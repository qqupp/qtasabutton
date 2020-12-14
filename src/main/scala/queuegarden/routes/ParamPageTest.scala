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

object ParamPageTest {

  def paramPage(values: List[PosInt]): String =
    s"""
      |<html>
      |<body>
      |Param $values page
      |</body>
      |</html>
      |""".stripMargin

  case class QueryParams01(
      b: Boolean,
      i: Int,
      s: Option[String]
    )

  case class PosInt2(
      fst: PosInt,
      snd: Option[PosInt]
    )

  case class QueryParams(
      b: PosInt2,
      d: QueryParams01
    )

  def route[F[_]: Applicative: Defer]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ GET -> Root / "param01" =>
        withQueryParams(
          required("posInt1", PosInt.matcher) &
            optional("posInt2", PosInt.matcher, PosInt.one) &
            optional("posInt3", PosInt.matcher, PosInt.one) &
            optional("posInt4", PosInt.matcher, PosInt.one) &
            optional("posInt5", PosInt.matcher, PosInt.one) &
            optional("posInt6", PosInt.matcher, PosInt.one) &
            optional("posInt7", PosInt.matcher, PosInt.one) &
            optional("posInt8", PosInt.matcher, PosInt.one) &
            optional("anInt", QueryParamDecoder.intQueryParamDecoder, 10)
            map flatten,
          req
        ) {
          case (a1, a2, a3, posInt, posInt2, a6, a7, a8, a9) =>
            Ok(paramPage(List(posInt, posInt2)), ContentType.html)
        }

      case req @ GET -> Root / "param02" =>
        withQueryParams(
          required("posInt1", PosInt.matcher),
          req
        ) {
          case x =>
            Ok(paramPage(List()), ContentType.html)
        }
    }
  }

}
