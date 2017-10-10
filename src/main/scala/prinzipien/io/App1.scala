package prinzipien.io

object App1 {
  val program: ConsoleIO2[Unit] =
    ConsoleIO2.PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      ConsoleIO2.PrintLine("What is your name?").flatMap { _ =>
        ConsoleIO2.ReadLine().flatMap { name =>
          ConsoleIO2.PrintLine(s"Hello, $name!")
        }
      }
    }

  def main(args: Array[String]): Unit =
    Interpreter1.unsafeRun(program)
}
