package prinzipien.kv

import prinzipien.kv.KVStore1._

sealed trait KVStore1[A] {
  def flatMap[B](f: A => KVStore1[B]): KVStore1[B] = FlatMap(this, f)
}

object KVStore1 {
  case class Get(key: String) extends KVStore1[Option[String]]
  case class Put(key: String, value: String) extends KVStore1[Unit]
  case class FlatMap[A, B](fa: KVStore1[A], f: A => KVStore1[B]) extends KVStore1[B]
}
