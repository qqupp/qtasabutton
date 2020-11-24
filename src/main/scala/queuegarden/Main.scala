package queuegarden

import cats.effect.{ExitCode, IO, IOApp}
import queuegarden.config.{ConfigLoader, ServerConfig}

object Main extends IOApp {

  val config = ConfigLoader.config

  val server = new Server(config.server)

  def run(args: List[String]): IO[ExitCode] = {
    server
      .stream[IO]
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
