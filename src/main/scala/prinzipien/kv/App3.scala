package prinzipien.kv

import prinzipien.kv.KVStore2._

object App3 {
  val program: KVStore2[String] =
    Put("firstname", "Frank").flatMap { _ =>
      Put("lastname", "Thomas").flatMap { _ =>
        Get("pet").flatMap {
          case Some(pet) => Pure(pet)
          case None => Pure("kein Haustier")
        }
      }
    }

  val program2: KVStore2[String] =
    for {
      _ <- Put("firstname", "Frank")
      _ <- Put("lastname", "Thomas")
      pet <- Get("pet")
    } yield pet.getOrElse("kein Haustier")

  def main(args: Array[String]): Unit =
    println(Interpreter2.run(program2).apply(Map("hometown" -> "Neusäß")))
}
