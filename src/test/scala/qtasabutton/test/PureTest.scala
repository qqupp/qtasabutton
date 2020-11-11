package qtasabutton.test

import cats.effect.IO

object PureTest {
  def apply[T](t: => IO[T]): T = t.unsafeRunSync()
}
