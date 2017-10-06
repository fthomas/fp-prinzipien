package prinzipien.app

import prinzipien.app.Task.FlatMap

sealed abstract class Task[A] {
  def flatMap[B](f: A => Task[B]): Task[B] =
    FlatMap(this, f)
}

object Task {
  case class Delay[A](f: () => A) extends Task[A]
  case class FlatMap[A, B](fa: Task[A], f: A => Task[B]) extends Task[B]
  case class Pure[A](a: A) extends Task[A]
}
