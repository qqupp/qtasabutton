package queuegarden.routes

import cats.Applicative
import org.http4s.dsl.Http4sDsl
import org.http4s.{ Request, Response }

package object params {

  def withQueryParam[F[_]: Applicative, A](
      p1: ParamExtractor[A],
      r: Request[F]
    )(
      handle: A => F[Response[F]]
    ): F[Response[F]] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    p1.extract(r.multiParams)
      .fold(
        nelE => BadRequest(nelE.toList.map(_.sanitized).mkString("\n")),
        handle(_)
      )
  }

}
