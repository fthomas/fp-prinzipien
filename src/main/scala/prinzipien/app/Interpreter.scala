package prinzipien.app

import prinzipien.app.Task.{Delay, FlatMap, Pure}

object Interpreter {
  def run[A](p: Task[A]): A =
    p match {
      case FlatMap(fa, f) =>
        val a = run(fa)
        run(f(a))
      case Delay(f) => f()
      case Pure(a) => a
    }
}
