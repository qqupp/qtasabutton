package queuegarden.routes

import cats.data.{ Kleisli, OptionT }
import cats.effect.Sync
import cats.{ Applicative, Defer, Monad }
import org.http4s.{ AuthedRoutes, HttpRoutes, Request, Response }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import queuegarden.authentication.Authentication
import queuegarden.domain.User

object Welcome {

  def authedRoutes[F[_]: Applicative: Defer]: AuthedRoutes[User, F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    AuthedRoutes.of[User, F] { case GET -> Root / "welcome" as user =>
      Ok(s"Welcome, ${user.name}")
    }
  }

  def routes[F[_]: Monad: Defer]: HttpRoutes[F] = {
    val f: AuthMiddleware[F, User] = Authentication.middleware[F]
    f(authedRoutes[F])
  }

}
