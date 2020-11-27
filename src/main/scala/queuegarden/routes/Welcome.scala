package queuegarden.routes

import cats.data.{ Kleisli, OptionT }
import cats.implicits._
import cats.{ Applicative, Defer, Monad }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRequest, AuthedRoutes, HttpRoutes, Response, Status }
import queuegarden.authentication.Authentication
import queuegarden.domain.{ AdminUser, User }

object Welcome {

  def authedRoutes[F[_]: Applicative: Defer]: AuthedRoutes[User, F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    AuthedRoutes.of[User, F] {
      case GET -> Root / "welcome" as user =>
        Ok(s"Welcome, ${user.name}")
    }
  }

  def authedRoutesRestricted[
      F[_]: Applicative: Defer
    ]: AuthedRoutes[AdminUser, F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    AuthedRoutes.of {
      case GET -> Root / "admin" as user =>
        Ok(s"Welcome restricted access, ${user.name}")
    }
  }

  def liftRestricted[F[_]: Applicative](
      adminOnlyRoutes: AuthedRoutes[AdminUser, F]
    ): AuthedRoutes[User, F] =
    Kleisli(req =>
      req.context match {
        case a: AdminUser => adminOnlyRoutes(AuthedRequest(a, req.req))
        case _            => OptionT.pure(Response[F](Status.Unauthorized))
      }
    )

  def routes[F[_]: Monad: Defer]: HttpRoutes[F] = {
    val f: AuthMiddleware[F, User] = Authentication.middleware[F]
    f(authedRoutes[F] <+> liftRestricted(authedRoutesRestricted[F]))
  }

}
