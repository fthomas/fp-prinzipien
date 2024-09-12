package prinzipien.db

import prinzipien.db.DB.{FlatMap, Pure}
import scala.annotation.tailrec

sealed trait DB[+A] {
  final def flatMap[B](f: A => DB[B]): DB[B] =
    FlatMap(this, f)

  final def map[B](f: A => B): DB[B] =
    flatMap(a => Pure(f(a)))
}

object DB {
  final case class Get(key: String) extends DB[Option[Int]]
  final case class Put(key: String, value: Int) extends DB[Unit]

  final case class Pure[A](a: A) extends DB[A]
  final case class FlatMap[A, B](fa: DB[A], f: A => DB[B]) extends DB[B]

  def runDB[A](fa: DB[A]): A = {
    @tailrec def loop(curr: DB[Any], stack: List[Any => DB[Any]], db: Map[String, Int]): Any = {
      curr match {
        case FlatMap(fa, f) => loop(fa, f :: stack, db)
        case _ =>
          val v = curr match {
            case Get(key)      => db.get(key)
            case Put(_, _)     => ()
            case Pure(a)       => a
            case FlatMap(_, _) => sys.error("impossible")
          }
          stack match {
            case Nil => v
            case h :: t =>
              curr match {
                case Put(key, value) => loop(h(v), t, db.updated(key, value))
                case _               => loop(h(v), t, db)
              }
          }
      }
    }

    loop(fa.asInstanceOf[DB[Any]], List.empty, Map.empty).asInstanceOf[A]
  }

  def main(args: Array[String]): Unit = {
    val prog =
      Put("frank", 5)
        .flatMap(_ => Pure("hello"))
        .flatMap(_ => Get("frank"))
        .map(_.map(_ + 5))
        .flatMap(v => Put("thomas", v.getOrElse(0)))
        .flatMap(_ => Get("thomas"))

    val prog2 = for {
      _ <- Put("frank", 4)
      v <- Get("frank").map(_.getOrElse(0))
      _ <- Put("foo", 1)
      v2 <- Get("foo").map(_.getOrElse(3))
    } yield v + v2

    println(runDB(prog2))
  }
}
