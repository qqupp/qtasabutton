package queuegarden.sql

import cats.effect.IO
import doobie._
import doobie.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import queuegarden.domain.User
import queuegarden.test._
import queuegarden.test.sql.DBOps

import scala.util.Right

class HomeMadeMigrationSpec extends AnyFlatSpec with Matchers {

  "migrate" should "create a schema with data if doesn't exist" in new DBOps {
    pureTest {
      for {
        r    <- new HomeMadeMigration[IO](testTransactor).migrate.attempt
        user <- getAdminUser.transact(testTransactor)
      } yield {
        r shouldBe a[Right[_, _]]
        user shouldBe User(0, "admin")
      }
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

  def getAdminUser = sql"""select * from user""".query[User].unique
}
