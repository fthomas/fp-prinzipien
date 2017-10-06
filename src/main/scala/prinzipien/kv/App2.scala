package prinzipien.kv

import prinzipien.kv.KVStore1._

object App2 {
  val program: KVStore1[Option[String]] =
    Put("firstname", "Frank").flatMap { _ =>
      Put("lastname", "Thomas").flatMap { _ =>
        Get("pet").flatMap {
          case Some(pet) => ??? // return pet
          case None => ??? // return "none"
        }
      }
    }
}
