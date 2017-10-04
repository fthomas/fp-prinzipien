package prinzipien

sealed abstract class ConsoleIO2[A] {
  def flatMap[B](f: A => ConsoleIO2[B]): ConsoleIO2[B] = ConsoleIO2.FlatMap(this, f)
}

object ConsoleIO2 {
  case class ReadLine() extends ConsoleIO2[String]
  case class PrintLine(string: String) extends ConsoleIO2[Unit]
  case class FlatMap[A, B](fa: ConsoleIO2[A], f: A => ConsoleIO2[B]) extends ConsoleIO2[B]
  case class Pure[A](a: A) extends ConsoleIO2[A]
}
