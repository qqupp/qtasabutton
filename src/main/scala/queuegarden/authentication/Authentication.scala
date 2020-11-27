package queuegarden.authentication

import cats.data.{ Kleisli, OptionT }
import cats.{ Applicative, Monad }
import org.http4s.Request
import org.http4s.server.AuthMiddleware
import queuegarden.domain.{ AdminUser, NormalUser, User }

object Authentication {

  def authUser[F[_]: Applicative]: Kleisli[OptionT[F, *], Request[F], User] =
    Kleisli { r =>
      OptionT.fromOption {
        val user = r.params.get("username")
        user.map(name =>
          if (name == "admin")
            AdminUser(0, name)
          else NormalUser(1, name)
        )
      }
    }

  def middleware[F[_]: Monad]: AuthMiddleware[F, User] =
    AuthMiddleware.apply(authUser[F])
}
