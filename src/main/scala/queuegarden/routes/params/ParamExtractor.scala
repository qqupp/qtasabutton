package queuegarden.routes.params

import cats.Applicative
import cats.data.ValidatedNel
import org.http4s.{ ParseFailure, QueryParamDecoder, QueryParameterValue }
import cats.data._
import cats.implicits._

trait ParamExtractor[T] { self =>

  def extract(
      params: Map[String, collection.Seq[String]]
    ): ValidatedNel[ParseFailure, T]

  def map[T1](f: T => T1): ParamExtractor[T1] =
    new ParamExtractor[T1] {
      def extract(
          params: Map[String, collection.Seq[String]]
        ): ValidatedNel[ParseFailure, T1] =
        self.extract(params).map(f)
    }

}

object ParamExtractor {

  def required[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T]
    ): ParamExtractor[T] = new ParamExtractor[T] {
    def extract(
        params: Map[String, collection.Seq[String]]
      ): ValidatedNel[ParseFailure, T] =
      params.get(name).flatMap(_.headOption) match {
        case None      =>
          val msg = s"Missing required parameter $name"
          Validated.invalidNel(
            ParseFailure(msg, msg)
          )
        case Some(str) => queryParamDecoder.decode(QueryParameterValue(str))
      }
  }

  def optional[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T]
    ): ParamExtractor[Option[T]] = new ParamExtractor[Option[T]] {
    def extract(
        params: Map[String, collection.Seq[String]]
      ): ValidatedNel[ParseFailure, Option[T]] =
      params
        .get(name)
        .flatMap(_.headOption)
        .map(str => queryParamDecoder.decode(QueryParameterValue(str)))
        .sequence

  }

  def optional[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T],
      default: T
    ): ParamExtractor[T] =
    optional(name, queryParamDecoder).map(_.getOrElse(default))

  implicit val parmaExtractorApplicaitve: Applicative[ParamExtractor] =
    new Applicative[ParamExtractor] {
      def pure[A](x: A): ParamExtractor[A] = new ParamExtractor[A] {
        def extract(
            params: Map[String, collection.Seq[String]]
          ): ValidatedNel[ParseFailure, A] = Validated.validNel(x)
      }

      def ap[A, B](
          ff: ParamExtractor[A => B]
        )(
          fa: ParamExtractor[A]
        ): ParamExtractor[B] = new ParamExtractor[B] {
        def extract(
            params: Map[String, collection.Seq[String]]
          ): ValidatedNel[ParseFailure, B] = {

          val eff: ValidatedNel[ParseFailure, A => B] = ff.extract(params)
          val efa: ValidatedNel[ParseFailure, A]      = fa.extract(params)
          (eff, efa).mapN { case (f, a) => f(a) }
        }
      }
    }

}
