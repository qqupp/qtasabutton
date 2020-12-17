package queuegarden

import cats.effect.{ Sync, Timer }
import fs2.Stream

import scala.concurrent.duration._

object InputStream {

  def inputConsumer[F[_]: Timer: Sync]: Stream[F, Int] =
    Stream(1).repeat.scanMonoid
      .covary[F]
      .metered(1 second)
  //.evalTap(i => Sync[F].delay(println(i)))

}
