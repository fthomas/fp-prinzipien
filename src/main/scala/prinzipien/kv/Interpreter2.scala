package prinzipien.kv

import prinzipien.kv.KVStore2._

object Interpreter2 {
  type Store = Map[String, String]

  def run[A](p: KVStore2[A]): Store => (Store, A) =
    p match {
      case Get(key) =>
        store => (store, store.get(key))

      case Put(key, value) =>
        store => (store.updated(key, value), ())

      case FlatMap(fa, f) =>
        val res1 = run(fa)
        store => {
          val (nextStore, a) = res1(store)
          val res2 = run(f(a))
          res2(nextStore)
        }

      case Pure(a) =>
        store => (store, a)
    }
}
