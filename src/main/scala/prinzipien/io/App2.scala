package prinzipien.io

import prinzipien.io.ConsoleIO2._

object App2 {
  val program: ConsoleIO2[Unit] =
    PrintLine("Awesome Greeter v1.0").flatMap { _ =>
      PrintLine("What is your name?").flatMap { _ =>
        ReadLine().flatMap { name =>
          if (name == "exit")
            ???
          else
            PrintLine(s"Hello, $name!").flatMap(_ => ???)
        }
      }
    }
}
