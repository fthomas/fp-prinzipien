package prinzipien.app

import prinzipien.app.ConsoleIO._
import prinzipien.app.Interpreter._
import prinzipien.app.KVStore._

object App {
  def prompt(line: String): Task[Unit] = {
    val tokens = line.split(' ')
    tokens.lift(0) match {
      case Some("get") if line.length > 4 =>
        val key = line.substring(4)
        Get(key).toTask.flatMap {
          case Some(value) => PrintLine(value).toTask
          case None => PrintLine(s"No value for '$key' found").toTask
        }

      case Some("put") if tokens.length >= 2 =>
        val key = tokens(1)
        val value = tokens.drop(2).mkString(" ")
        Put(key, value).toTask.flatMap(_ => PrintLine(s"Added '$key -> $value' to store").toTask)

      case _ =>
        PrintLine(s"Unknown or malformed command: $line").toTask
    }
  }

  val loop: Task[Unit] =
    ReadLine().toTask.flatMap { line =>
      if (line == "exit")
        PrintLine("Good bye!").toTask
      else
        prompt(line).flatMap(_ => loop)
    }

  val program: Task[Unit] =
    PrintLine("Awesome KV Store v1.0").toTask.flatMap(_ => loop)

  def main(args: Array[String]): Unit =
    Interpreter.unsafeRun(program)
}
