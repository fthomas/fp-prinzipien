package prinzipien.app

import prinzipien.app.ConsoleIO._
import prinzipien.app.KVStore._
import prinzipien.app.Task._

object Interpreter {

  implicit class ConsoleIOOps[A](val self: ConsoleIO[A]) {
    def toTask: Task[A] = impl(self)

    private def impl[B](p: ConsoleIO[B]): Task[B] =
      p match {
        case ReadLine() =>
          Delay(() => scala.Console.readLine())

        case PrintLine(string) =>
          Delay(() => scala.Console.println(string))
      }
  }

  implicit class KVStoreOps[A](val self: KVStore[A]) {
    def toTask: Task[A] = impl(self)

    private def impl[B](p: KVStore[B]): Task[B] =
      p match {
        case Get(key) =>
          Delay(() => Option(java.lang.System.getProperty(key)))

        case Put(key, value) =>
          Delay(() => {
            java.lang.System.setProperty(key, value)
            ()
          })
      }
  }

  def unsafeRun[A](p: Task[A]): A =
    p match {
      case FlatMap(fa, f) =>
        val a = unsafeRun(fa)
        unsafeRun(f(a))

      case Delay(f) => f()

      case Pure(a) => a
    }
}
