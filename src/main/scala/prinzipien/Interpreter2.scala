package prinzipien

import prinzipien.ConsoleIO2._

object Interpreter2 {
  def run[A](p: ConsoleIO2[A]): A =
    p match {
      case ReadLine() => scala.Console.readLine()
      case PrintLine(string) => scala.Console.println(string)
      case FlatMap(fa, f) =>
        val a = run(fa)
        run(f(a))
      case Pure(a) => a
    }
}
