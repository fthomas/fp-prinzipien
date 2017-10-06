package prinzipien.io

sealed abstract class ConsoleIO1[A] {
  def flatMap[B](f: A => ConsoleIO1[B]): ConsoleIO1[B] = ConsoleIO1.FlatMap(this, f)
}

object ConsoleIO1 {
  case class ReadLine() extends ConsoleIO1[String]
  case class PrintLine(string: String) extends ConsoleIO1[Unit]
  case class FlatMap[A, B](fa: ConsoleIO1[A], f: A => ConsoleIO1[B]) extends ConsoleIO1[B]
}
