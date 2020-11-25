package queuegarden.sql

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import queuegarden.test.sql.DBOps
import queuegarden.test._
import cats._
import cats.implicits._
import cats.effect.IO

import scala.util.Right

class HomeMadeMigrationSpec extends AnyFlatSpec with Matchers {

  "migrate" should "create a schema if doesn't exist" in new DBOps {
    pureTest {
      for {
        r <- new HomeMadeMigration[IO](testTransactor).migrate.attempt
      } yield r shouldBe a[Right[_, _]]
    }
  }

  it should "not fail if the schema already exist" in new DBOps {
    pureTest {
      for {
        migration <- IO(new HomeMadeMigration[IO](testTransactor))
        r1        <- migration.migrate.attempt
        r2        <- migration.migrate.attempt
      } yield r2 shouldBe a[Right[_, _]]
    }
  }

}
