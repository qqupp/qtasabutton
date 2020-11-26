package queuegarden.authentication

import cats.data.{Kleisli, OptionT}
import cats.{Applicative, Monad}
import org.http4s.Request
import org.http4s.server.AuthMiddleware
import queuegarden.domain.User

object Authentication {

  def authUser[F[_]: Applicative]: Kleisli[OptionT[F, *], Request[F], User] =
    Kleisli { r =>
      OptionT.fromOption {
        val user = r.params.get("username")
        user.map(User(0, _))
      }
    }

  def middleware[F[_]: Monad]: AuthMiddleware[F, User] =
    AuthMiddleware(authUser[F])

}