package prinzipien.io

object App2 {
  val program: ConsoleIO2[Unit] =
    ConsoleIO2.PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      ConsoleIO2.PrintLine("What is your name?").flatMap { _ =>
        ConsoleIO2.ReadLine().flatMap { name =>
          if (name == "exit")
            ???
          else
            ConsoleIO2.PrintLine(s"Hello, $name!").flatMap(_ => ???)
        }
      }
    }
}
