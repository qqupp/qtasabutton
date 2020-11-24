package queuegarden.sql

import cats.effect.IO
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import queuegarden.test._
import doobie._
import doobie.implicits._
import queuegarden.config.instances.DBTestConfig

class DBTransactorSpec
    extends AnyFlatSpec
    with Matchers
    with IOSynchronousTestExecutionContext {

  "db transactor" should "open a db connection with sqllite" in pendingTest {
    pending
    val transactor = new DBTransactor(DBTestConfig.config).xa[IO]
    createTestTable
      .transact(transactor)
      .flatMap(result => IO(result shouldBe 1))
  }

  val createTestTable: doobie.ConnectionIO[Int] =
    sql"""
    CREATE TABLE TEST (
      id INTEGER NOT NULL UNIQUE,
    )
  """.update.run

}
