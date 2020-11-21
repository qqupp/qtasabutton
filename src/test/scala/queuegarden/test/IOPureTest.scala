package queuegarden.test

import cats.effect.IO

object IOPureTest {
  def apply[T](t: => IO[T]): T = t.unsafeRunSync()
}
