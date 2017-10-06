package prinzipien.app

/*
class Combined[F[_]: Monad](store: KVStore[F], console: ConsoleIO[F]) {

  def program: F[Unit] =
    (console.println("bla"): F[Unit]).flatMap(_ => store.get("foo"): F[Option[String]])
}
*/

object App1 {
  def prompt(line: String): Task[Unit] = {
    val tokens = line.split(' ')
    tokens.lift(0) match {
      case Some("get") =>
        val key = line.substring(4)
        KVStore.Get(key).toTask.flatMap {
          case Some(value) => ConsoleIO.PrintLine(value).toTask
          case None => ConsoleIO.PrintLine(s"No value for $key found").toTask
        }
      case Some("put") if tokens.length >= 2 =>
        val key = tokens(1)
        val value = tokens.drop(2).mkString(" ")
        KVStore.Put(key, value).toTask
      case _ =>
        ConsoleIO.PrintLine(s"Unknown or malformed command: $line").toTask
    }
  }

  val loop: Task[Unit] =
    ConsoleIO.ReadLine().toTask.flatMap { line =>
      if (line == "exit")
        ConsoleIO.PrintLine("Good bye!").toTask
      else
        prompt(line).flatMap(_ => loop)
    }

  val program: Task[Unit] =
    ConsoleIO.PrintLine("Awesome KV Console v1.0").toTask.flatMap(_ => loop)
}
