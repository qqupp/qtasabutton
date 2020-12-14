package queuegarden.routes.params

import cats.data.ValidatedNel
import org.http4s.ParseFailure
import cats.data._
import cats.implicits._

trait ParamExtractor[T] { self =>

  def unapply(
      params: Map[String, collection.Seq[String]]
    ): Option[ValidatedNel[ParseFailure, T]]

  def map[T1](f: T => T1): ParamExtractor[T1] =
    new ParamExtractor[T1] {
      def unapply(
          params: Map[String, collection.Seq[String]]
        ): Option[ValidatedNel[ParseFailure, T1]] =
        self.unapply(params).map(_.map(f))
    }

}

object ParamExtractor {

  def comb2[A, B](
      p1: ParamExtractor[A],
      p2: ParamExtractor[B]
    ): ParamExtractor[(A, B)] = new ParamExtractor[(A, B)] {
    def unapply(
        params: Map[String, collection.Seq[String]]
      ): Option[ValidatedNel[ParseFailure, (A, B)]] =
      (p1.unapply(params), p2.unapply(params)).mapN((p1res, p2res) =>
        (p1res, p2res).mapN((_, _))
      )
  }

  implicit class ParamExtractorOps[A](e1: ParamExtractor[A]) {
    def &[B](e2: ParamExtractor[B]): ParamExtractor[(A, B)] =
      comb2(e1, e2)
  }

  import shapeless._
  import ops.tuple.FlatMapper
  import syntax.std.tuple._

  trait LowPriorityFlatten extends Poly1              {
    implicit def default[T] = at[T](Tuple1(_))
  }
  object flatten           extends LowPriorityFlatten {
    implicit def caseTuple[P <: Product](
        implicit
        lfm: Lazy[FlatMapper[P, flatten.type]]
      ) =
      at[P](lfm.value(_))
  }

}
