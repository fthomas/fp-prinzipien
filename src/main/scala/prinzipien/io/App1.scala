package prinzipien.io

object App1 {
  val program: ConsoleIO1[Unit] =
    ConsoleIO1.PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      ConsoleIO1.PrintLine("What is your name?").flatMap { _ =>
        ConsoleIO1.ReadLine().flatMap { name =>
          ConsoleIO1.PrintLine(s"Hello, $name!")
        }
      }
    }
}
