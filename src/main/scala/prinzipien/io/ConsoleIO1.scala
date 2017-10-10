package prinzipien.io

sealed trait ConsoleIO1[A]

object ConsoleIO1 {
  case class ReadLine() extends ConsoleIO1[String]
  case class PrintLine(string: String) extends ConsoleIO1[Unit]
}
