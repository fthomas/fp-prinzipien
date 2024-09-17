package prinzipien.db

import prinzipien.db.DB2.{FlatMap, HandleErrorWith, Pure}
import scala.annotation.tailrec

sealed trait DB2[+A] {
  final def flatMap[B](f: A => DB2[B]): DB2[B] =
    FlatMap(this, f)

  final def map[B](f: A => B): DB2[B] =
    flatMap(a => Pure(f(a)))

  final def handleErrorWith[B](f: Throwable => DB2[B]): DB2[B] =
    HandleErrorWith(this, f)
}

object DB2 {
  final case class Get(key: String) extends DB2[Option[Int]]
  final case class Put(key: String, value: Int) extends DB2[Unit]

  final case class Pure[A](a: A) extends DB2[A]
  final case class FlatMap[A, B](fa: DB2[A], f: A => DB2[B]) extends DB2[B]

  final case class RaiseError[A](e: Throwable) extends DB2[A]
  final case class HandleErrorWith[A, B](fa: DB2[A], f: Throwable => DB2[B]) extends DB2[B]

  val unit: DB2[Unit] = Pure(())

  def runDB[A](dbA: DB2[A]): A = {
    @tailrec
    def loop(
        curr: DB2[Any],
        stack: List[Either[Throwable => DB2[Any], Any => DB2[Any]]],
        db: Map[String, Int]
    ): Any =
      curr match {
        case FlatMap(fa, f)         => loop(fa, Right(f) :: stack, db)
        case HandleErrorWith(fa, f) => loop(fa, Left(f) :: stack, db)
        case RaiseError(e) =>
          stack match {
            case Nil           => throw e
            case Left(h) :: t  => loop(h(e), t, db)
            case Right(_) :: t => loop(curr, t, db)
          }
        case _ =>
          def v = curr match {
            case Get(key)              => db.get(key)
            case Put(_, _)             => ()
            case Pure(a)               => a
            case FlatMap(_, _)         => sys.error("impossible")
            case RaiseError(_)         => sys.error("impossible")
            case HandleErrorWith(_, _) => sys.error("impossible")
          }
          stack match {
            case Nil          => v
            case Left(_) :: t => loop(curr, t, db)
            case Right(h) :: t =>
              curr match {
                case Put(key, value) => loop(h(v), t, db.updated(key, value))
                case _               => loop(h(v), t, db)
              }
          }
      }

    loop(dbA, List.empty, Map.empty).asInstanceOf[A]
  }

  def main(args: Array[String]): Unit = {
    val prog1 = RaiseError[Unit](new Throwable("boom 1"))
    val prog2 = RaiseError[Unit](new Throwable("boom 2")).handleErrorWith(_ => Pure("hello"))
    val prog3 = RaiseError[Unit](new Throwable("boom 3")).handleErrorWith(_ => Get("hello"))
    val prog4 = Pure("hello").flatMap(_ => RaiseError[Unit](new Throwable("boom 4")))
    val prog5 = Pure("hello")
      .flatMap(_ => RaiseError[Unit](new Throwable("boom 5")))
      .map(identity)
      .handleErrorWith(_ => Pure("world 5"))
    val prog6 = Pure("hello")
      .flatMap(_ => RaiseError[Unit](new Throwable("boom 6")))
      .map(identity)
      .map(identity)
      .handleErrorWith(_ => Pure("world 6"))

    val prog7 = (for {
      _ <- Put("frank", 4)
      v <- Get("frank").map(_.getOrElse(0))
      _ <- RaiseError[Unit](new Throwable("boom"))
      _ <- Put("foo", 1)
      v2 <- Get("foo").map(_.getOrElse(3))
    } yield v + v2).handleErrorWith(_ => Get("frank").map(_.map(_ + 7)))

    println(runDB(prog7))
  }
}
