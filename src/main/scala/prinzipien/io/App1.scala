package prinzipien.io

import prinzipien.io.ConsoleIO2._

object App1 {
  val program: ConsoleIO2[Unit] =
    PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      PrintLine("What is your name?").flatMap { _ =>
        ReadLine().flatMap { name =>
          PrintLine(s"Hello, $name!")
        }
      }
    }

  def main(args: Array[String]): Unit =
    Interpreter1.unsafeRun(program)
}
