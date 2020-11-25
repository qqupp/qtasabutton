package queuegarden.sql

import java.util.concurrent.Executors

import cats.effect.{Async, Blocker, ContextShift}
import doobie.util.transactor.Transactor
import queuegarden.config.DBConfig

import scala.concurrent.ExecutionContext

class DBTransactor(config: DBConfig) {

  private val dbBlockingEc =
    ExecutionContext.fromExecutor(
      Executors.newFixedThreadPool(config.queryConcurrencyLevel)
    )

  def xa[F[_]: Async: ContextShift]: Transactor[F] =
    Transactor.fromDriverManager(
      "org.sqlite.JDBC",
      s"jdbc:sqlite:${config.dbPath}",
      config.user,
      config.password,
      Blocker.liftExecutionContext(
        dbBlockingEc
      )
    )

}
