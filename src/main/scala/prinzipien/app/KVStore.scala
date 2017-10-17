package prinzipien.app

sealed trait KVStore[A]

object KVStore {
  case class Get(key: String) extends KVStore[Option[String]]
  case class Put(key: String, value: String) extends KVStore[Unit]
}
