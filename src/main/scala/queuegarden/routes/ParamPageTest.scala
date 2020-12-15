package queuegarden.routes

import cats.data._
import cats.implicits._
import cats.{ Applicative, Defer }
import org.http4s.dsl.Http4sDsl
import org.http4s.{
  HttpRoutes,
  ParseFailure,
  QueryParamDecoder,
  Request,
  Response
}
import queuegarden.routes.params._
import queuegarden.routes.params.PosInt._
import ParamExtractor._
import cats.data.Validated.Valid

object ParamPageTest {

  def paramPage(
      values: List[PosInt],
      qp: Option[QueryParamsForThisRoute]
    ): String =
    s"""
      |<html>
      |<body>
      |Param $values page
      |$qp
      |</body>
      |</html>
      |""".stripMargin

  case class QueryParams01(
      b: Boolean,
      i: Int,
      c: Char,
      b1: Boolean,
      s: Option[String]
    )

  case class PosInt2(
      fst: PosInt,
      snd: Option[PosInt]
    )

  lazy val posInt2Extractor: ParamExtractor[PosInt2] =
    (
      required("fst", PosInt.queryParamDecoder),
      optional("snd", PosInt.queryParamDecoder)
    ).mapN(PosInt2)

  case class QueryParamsForThisRoute(
      bb: PosInt2,
      dd: QueryParams01
    )

  lazy val queryParamsForThisRouteExtractor
      : ParamExtractor[QueryParamsForThisRoute] =
    (
      posInt2Extractor,
      queryParams01Extractor
    ).mapN(QueryParamsForThisRoute)

  lazy val queryParams01Extractor: ParamExtractor[QueryParams01] =
    (
      optional("b", QueryParamDecoder.booleanQueryParamDecoder, false),
      required("i", QueryParamDecoder.intQueryParamDecoder),
      required("c", QueryParamDecoder.charQueryParamDecoder),
      required("b", QueryParamDecoder.booleanQueryParamDecoder),
      optional("s", QueryParamDecoder.stringQueryParamDecoder)
    ).mapN(QueryParams01)

  def route[F[_]: Applicative: Defer]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ GET -> Root / "param01" =>
        withQueryParam(
          (
            required("posInt1", PosInt.queryParamDecoder),
            optional("posInt2", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt3", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt4", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt5", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt6", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt7", PosInt.queryParamDecoder, PosInt.one),
            optional("posInt8", PosInt.queryParamDecoder, PosInt.one),
            optional("anInt", QueryParamDecoder.intQueryParamDecoder, 10)
          ).mapN(Tuple9.apply),
          req
        ) {
          case (a1, a2, a3, posInt, posInt2, a6, a7, a8, a9) =>
            Ok(paramPage(List(posInt, posInt2), None), ContentType.html)
        }

      case req @ GET -> Root / "param02" =>
        withQueryParam(
          queryParamsForThisRouteExtractor,
          req
        ) {
          case x =>
            Ok(paramPage(List(), Some(x)), ContentType.html)
        }

      case req @ GET -> Root / "param03"
          :? queryParamsForThisRouteExtractor(roba) =>
        roba.fold(
          _ => BadRequest(),
          data => Ok(paramPage(List(), Some(data)), ContentType.html)
        )
    }
  }

}
