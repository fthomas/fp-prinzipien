package prinzipien.kv

import prinzipien.kv.KVStore1._

object App1 {
  val program: KVStore1[Option[String]] =
    Put("firstname", "Frank").flatMap { _ =>
      Put("lastname", "Thomas").flatMap { _ =>
        Put("first_pet", "1b80a9a0-a334-4f96-97e6-7ef91ccd2189").flatMap { _ =>
          Get("firstname")
        }
      }
    }

  def main(args: Array[String]): Unit = {
    println(Interpreter1.run(program).apply(Map("hometown" -> "Neusäß")))
  }
}
