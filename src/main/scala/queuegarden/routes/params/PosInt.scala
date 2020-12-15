package queuegarden.routes.params

import org.http4s.{ ParseFailure, QueryParamDecoder }

sealed abstract case class PosInt(value: Int)

object PosInt {

  val one = new PosInt(1) {}

  val queryParamDecoder: QueryParamDecoder[PosInt] =
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
