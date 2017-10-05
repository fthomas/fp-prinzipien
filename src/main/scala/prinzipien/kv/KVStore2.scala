package prinzipien.kv

import prinzipien.kv.KVStore2._

sealed abstract class KVStore2[A] {
  def flatMap[B](f: A => KVStore2[B]): KVStore2[B] = FlatMap(this, f)
  def map[B](f: A => B): KVStore2[B] = FlatMap(this, f.andThen(Pure.apply))
}

object KVStore2 {
  case class Get(key: String) extends KVStore2[Option[String]]
  case class Put(key: String, value: String) extends KVStore2[Unit]
  case class FlatMap[A, B](fa: KVStore2[A], f: A => KVStore2[B]) extends KVStore2[B]
  case class Pure[A](a: A) extends KVStore2[A]
}
