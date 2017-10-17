package prinzipien.io

import prinzipien.io.ConsoleIO2._

object Interpreter1 {
  def unsafeRun[A](self: ConsoleIO2[A]): A =
    self match {
      case ReadLine() =>
        scala.Console.readLine()

      case PrintLine(string) =>
        scala.Console.println(string)

      case FlatMap(fa, f) =>
        val a = unsafeRun(fa)
        unsafeRun(f(a))
    }
}
