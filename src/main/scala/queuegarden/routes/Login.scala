package queuegarden.routes

import cats.{ Applicative, Defer }
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, Status }

object Login {

  val loginPage: String =
    """
      |<html>
      |<body>
      |Login page
      |</body>
      |</html>
      |""".stripMargin

  def route[F[_]: Applicative: Defer]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root / "login" =>
      val o: Status.Ok.type = Ok
      Ok(loginPage, ContentType.html)
    }
  }
}
