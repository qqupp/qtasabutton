package queuegarden.routes

import cats.Applicative
import cats.data.ValidatedNel
import org.http4s.{ ParseFailure, QueryParamDecoder, Request, Response }
import org.http4s.dsl.Http4sDsl
import cats._
import cats.data._
import cats.implicits._
import org.http4s.dsl.impl.{
  OptionalValidatingQueryParamDecoderMatcher,
  ValidatingQueryParamDecoderMatcher
}
import queuegarden.routes.params.ParamExtractor.flatten

package object params {

  def withQueryParams[F[_]: Applicative, A](
      p1: ParamExtractor[A],
      r: Request[F]
    )(
      handle: A => F[Response[F]]
    ): F[Response[F]] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    p1.unapply(r.multiParams) match {
      case None            => BadRequest()
      case Some(validated) =>
        validated
          .fold(
            nelE => BadRequest(nelE.toList.map(_.sanitized).mkString("\n")),
            handle(_)
          )
    }
  }

  def required[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T]
    ): ValidatingQueryParamDecoderMatcher[T] with ParamExtractor[T] =
    new ValidatingQueryParamDecoderMatcher[T](name)(queryParamDecoder)
      with ParamExtractor[T]

  def optionalParam[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T]
    ): OptionalValidatingQueryParamDecoderMatcher[T]                =
    new OptionalValidatingQueryParamDecoderMatcher[T](name)(
      queryParamDecoder
    ) {}

  def optional[T](
      name: String,
      queryParamDecoder: QueryParamDecoder[T],
      default: T
    ): ValidatingQueryParamDecoderMatcher[T] with ParamExtractor[T] =
    new ValidatingQueryParamDecoderMatcher(name: String)(queryParamDecoder)
      with ParamExtractor[T] {
      private val optP                           = optionalParam(name, queryParamDecoder)
      override def unapply(
          params: Map[String, collection.Seq[String]]
        ): Option[ValidatedNel[ParseFailure, T]] =
        optP.unapply(params).map(_.getOrElse(Validated.validNel(default)))
    }
}
