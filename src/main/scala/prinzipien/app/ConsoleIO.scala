package prinzipien.app

sealed trait ConsoleIO[A]

object ConsoleIO {
  case class ReadLine() extends ConsoleIO[String]
  case class PrintLine(string: String) extends ConsoleIO[Unit]
}
