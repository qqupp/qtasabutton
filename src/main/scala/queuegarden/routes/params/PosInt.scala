package queuegarden.routes.params

import cats.data.{ Validated, ValidatedNel }
import org.http4s.dsl.impl.{
  OptionalValidatingQueryParamDecoderMatcher,
  QueryParamDecoderMatcher,
  ValidatingQueryParamDecoderMatcher
}
import org.http4s.{ ParseFailure, QueryParamDecoder, QueryParameterValue }
import queuegarden.decoding._

sealed abstract case class PosInt(value: Int)

object PosInt {

  val one = new PosInt(1) {}

//
//  val o = QueryParamDecoder
//
//  private val posIntDecoder: QueryParamDecoder[Int, PosInt] =
//    Decoder { i =>
//      Either.cond(
//        i > 0,
//        new PosInt(i) {},
//        QueryParamError(s"$i must be positive")
//      )
//    }
//
//  def from: QueryParamDecoder[String, PosInt] =
//    integerDecoder andThen posIntDecoder

  val matcher: QueryParamDecoder[PosInt] =
    QueryParamDecoder.intQueryParamDecoder.emap { int =>
      Either.cond(
        int > 0,
        new PosInt(int) {},
        ParseFailure(
          "sanitized cant create posInt",
          s"details $int must be greater than zero"
        )
      )
    }

}
