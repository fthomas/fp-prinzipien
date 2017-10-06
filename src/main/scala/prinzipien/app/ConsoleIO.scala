package prinzipien.app

import prinzipien.app.ConsoleIO.{FlatMap, PrintLine, Pure, ReadLine}

sealed abstract class ConsoleIO[A] {
  def flatMap[B](f: A => ConsoleIO[B]): ConsoleIO[B] = FlatMap(this, f)
  def map[B](f: A => B): ConsoleIO[B] = flatMap(f.andThen(Pure.apply))
  def toTask: Task[A] =
    this match {
      case _: ReadLine => Task.Delay(() => scala.Console.readLine()).asInstanceOf[Task[A]]
      case x: PrintLine => Task.Delay(() => scala.Console.println(x.string)).asInstanceOf[Task[A]]
      case _ => ???
    }
}

object ConsoleIO {
  case class ReadLine() extends ConsoleIO[String]
  case class PrintLine(string: String) extends ConsoleIO[Unit]
  case class FlatMap[A, B](fa: ConsoleIO[A], f: A => ConsoleIO[B]) extends ConsoleIO[B]
  case class Pure[A](a: A) extends ConsoleIO[A]
}
