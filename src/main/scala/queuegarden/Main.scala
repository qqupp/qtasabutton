package queuegarden

import cats.effect.{ExitCode, IO, IOApp}
import queuegarden.config.ServerConfig

object Main extends IOApp {

  val server = new Server(ServerConfig(port = 8080))

  def run(args: List[String]): IO[ExitCode] = {
    server.stream[IO]
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
