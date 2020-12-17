package queuegarden

import cats.effect.{ Sync, Timer }
import fs2.Stream

import scala.concurrent.duration._

trait Memory[F[_]] {
  def set(i: Int): F[Unit]
  def get: F[Int]
}

class VarMemory[F[_]: Sync] extends Memory[F] {
  private var _integer: Int = 0
  def set(i: Int): F[Unit]  = Sync[F].delay { _integer = i }
  def get: F[Int]           = Sync[F].delay { _integer }
}

object InputStream {

  def inputConsumer[F[_]: Timer: Sync](memory: Memory[F]): Stream[F, Int] =
    Stream(1).repeat.scanMonoid
      .covary[F]
      .metered(1 second)
      .evalTap(i => memory.set(i))

}
