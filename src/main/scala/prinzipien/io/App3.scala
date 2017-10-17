package prinzipien.io

import prinzipien.io.ConsoleIO2._

object App3 {
  val loop: ConsoleIO2[Unit] =
    PrintLine("What is your name?").flatMap { _ =>
      ReadLine().flatMap { name =>
        if (name == "exit")
          PrintLine("Good bye!")
        else
          PrintLine(s"Hello, $name!").flatMap { _ => loop }
      }
    }

  val program: ConsoleIO2[Unit] =
    PrintLine("Awesome Greeter v1.0").flatMap { _ => loop }

  def main(args: Array[String]): Unit =
    Interpreter1.unsafeRun(program)
}
