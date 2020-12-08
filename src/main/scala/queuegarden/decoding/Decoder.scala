package queuegarden.decoding

import cats._
import cats.data._
import cats.implicits._

object Decoder {

  /*
    A Decoder is a convenient wrapper around a function
      I => Either[E, O]

    so you can chain decoders
      d1 andThen d2

    recover erroring decoders
      d1 orElse d2

    map and flatmap to transform it
   */
  type Decoder[-I, E, O] = Kleisli[Either[E, *], I, O]

  def apply[I, E, O](f: I => Either[E, O]): Decoder[I, E, O] =
    Kleisli(f)

  def toTraversable[F[_]: Traverse, I, E, O](
      d: Decoder[I, E, O]
    ): Decoder[F[I], E, F[O]] =
    Decoder(f => f.traverse(d(_)))

  def orElse[I, E, O](
      d1: Decoder[I, E, O],
      d2: Decoder[I, E, O]
    ): Decoder[I, E, O] =
    d1 <+> d2

  implicit class DecoderOps[I, E, O](d1: Kleisli[Either[E, *], I, O]) {
    def orElse(d2: => Decoder[I, E, O]): Decoder[I, E, O] =
      Decoder.orElse(d1, d2)

    def toTraversable[F[_]: Traverse]: Decoder[F[I], E, F[O]] =
      Decoder.toTraversable(d1)
  }

}
