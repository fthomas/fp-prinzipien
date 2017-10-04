package prinzipien

import prinzipien.ConsoleIO1._

object Interpreter1 {
  def run[A](p: ConsoleIO1[A]): A =
    p match {
      case ReadLine() => scala.Console.readLine()
      case PrintLine(string) => scala.Console.println(string)
      case FlatMap(fa, f) =>
        val a = run(fa)
        run(f(a))
    }
}
