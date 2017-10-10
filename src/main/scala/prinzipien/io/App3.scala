package prinzipien.io

object App3 {
  val loop: ConsoleIO2[Unit] =
    ConsoleIO2.PrintLine("What is your name?").flatMap { _ =>
      ConsoleIO2.ReadLine().flatMap { name =>
        if (name == "exit")
          ConsoleIO2.PrintLine("Good bye!")
        else
          ConsoleIO2.PrintLine(s"Hello, $name!").flatMap { _ => loop }
      }
    }

  val program: ConsoleIO2[Unit] =
    ConsoleIO2.PrintLine("Awesome Greeter v1.0").flatMap { _ => loop }

  def main(args: Array[String]): Unit =
    Interpreter1.unsafeRun(program)
}
