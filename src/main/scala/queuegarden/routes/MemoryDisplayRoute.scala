package queuegarden.routes

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import queuegarden.Memory

object MemoryDisplayRoute {

  def page(i: Int): String =
    s"""
       |<html>
       |<body>
       |Memory $i
       |</body>
       |</html>
       |""".stripMargin

  def route[F[_]: Sync](m: Memory[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "memory" =>
        m.get.flatMap { i =>
          Ok(page(i), ContentType.html)
        }
    }
  }
}
