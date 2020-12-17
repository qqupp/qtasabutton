package queuegarden

import cats.effect.{ ExitCode, IO, IOApp }
import doobie.util.transactor.Transactor
import fs2._
import queuegarden.config.ConfigLoader
import queuegarden.sql.{ DBTransactor, HomeMadeMigration }

object Main extends IOApp {

  val config = ConfigLoader.config

  val serverStream: Stream[IO, ExitCode] =
    new Server(config.server)
      .stream[IO]

  val inputStream: Stream[IO, Int] = InputStream.inputConsumer

  val runStreams: IO[Unit] =
    Stream(serverStream, inputStream)
      .parJoin(2)
      .compile
      .drain

  val DBTransactor: Transactor[IO] =
    new DBTransactor(config.database).xa[IO]

  val runDBMigration: IO[Unit] =
    new HomeMadeMigration(DBTransactor).migrate

  def run(args: List[String]): IO[ExitCode] = {
    runDBMigration *> runStreams
      .as(ExitCode.Success)
  }

}
