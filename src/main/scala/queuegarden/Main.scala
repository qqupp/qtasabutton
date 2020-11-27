package queuegarden

import cats.effect.{ ExitCode, IO, IOApp }
import doobie.util.transactor
import doobie.util.transactor.Transactor
import queuegarden.config.{ ConfigLoader, ServerConfig }
import queuegarden.sql.{ DBTransactor, HomeMadeMigration }
import cats.implicits._

object Main extends IOApp {

  val config = ConfigLoader.config

  val runServer: IO[Unit] =
    new Server(config.server)
      .stream[IO]
      .compile
      .drain

  val DBTransactor: Transactor[IO] =
    new DBTransactor(config.database).xa[IO]

  val runDBMigration: IO[Unit] =
    new HomeMadeMigration(DBTransactor).migrate

  def run(args: List[String]): IO[ExitCode] = {
    runDBMigration *> runServer
      .as(ExitCode.Success)
  }

}
