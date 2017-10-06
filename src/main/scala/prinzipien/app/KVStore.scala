package prinzipien.app

import prinzipien.app.KVStore.{FlatMap, Get, Pure, Put}

trait KVStore2[F[_]] {
  def get(key: String): F[Option[String]]
}

object TaskStore extends KVStore2[Task] {
  override def get(key: String):Task[Option[String]] = ???
}

sealed abstract class KVStore[A] {
  def flatMap[B](f: A => KVStore[B]): KVStore[B] = FlatMap(this, f)
  def map[B](f: A => B): KVStore[B] = flatMap(f.andThen(Pure.apply))
  def toTask: Task[A] =
    this match {
      case x: Get => Task.Delay(() => Option(java.lang.System.getProperty(x.key))).asInstanceOf[Task[A]]
      case x: Put => Task.Delay(() =>  { java.lang.System.setProperty(x.key, x.value); () }).asInstanceOf[Task[A]]
      case _ => ???
    }
}

object KVStore {
  case class Get(key: String) extends KVStore[Option[String]]
  case class Put(key: String, value: String) extends KVStore[Unit]
  case class FlatMap[A, B](fa: KVStore[A], f: A => KVStore[B]) extends KVStore[B]
  case class Pure[A](a: A) extends KVStore[A]
}
