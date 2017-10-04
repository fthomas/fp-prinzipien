package prinzipien

object App2 {
  val program: ConsoleIO1[Unit] =
    ConsoleIO1.PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      ConsoleIO1.PrintLine("What is your name?").flatMap { _ =>
        ConsoleIO1.ReadLine().flatMap { name =>
          if (name == "exit")
            ???
          else
            ConsoleIO1.PrintLine(s"Hello, $name!").flatMap(_ => ???)
        }
      }
    }

  val loop: ConsoleIO1[Unit] =
    ConsoleIO1.PrintLine("What is your name?").flatMap { _ =>
      ConsoleIO1.ReadLine().flatMap { name =>
        if (name == "exit")
          ConsoleIO1.PrintLine("Good bye!")
        else
          ConsoleIO1.PrintLine(s"Hello, $name!").flatMap { _ => loop }
      }
    }

  val program2: ConsoleIO1[Unit] =
    ConsoleIO1.PrintLine("Awesome Greeter v1.0").flatMap { _ => loop }
}
