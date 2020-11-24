package queuegarden.sql

import cats.effect.{Async, Blocker, ContextShift}
import doobie.util.transactor.Transactor
import queuegarden.config.DBConfig

class DBTransactor(config: DBConfig) {

  def xa[F[_]: Async: ContextShift] = Transactor.fromDriverManager(
    "org.sqlite.JDBC",
    "jdbc:sqlite:sample.db",
    config.user,
    config.password,
    Blocker.liftExecutionContext(
      scala.concurrent.ExecutionContext.Implicits.global
    )
  )

}
