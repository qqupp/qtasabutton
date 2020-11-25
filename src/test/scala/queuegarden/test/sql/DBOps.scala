package queuegarden.test.sql

import cats.effect.IO
import doobie.util.transactor.Transactor
import queuegarden.config.DBConfig
import queuegarden.sql.DBTransactor
import queuegarden.test.IOSynchronousTestExecutionContext

import scala.util.Random

trait DBOps extends IOSynchronousTestExecutionContext { self =>

  val testDBConfig = DBConfig(
    s"/tmp/${self.getClass.getName}-test${Random.nextInt()}.db",
    "",
    ""
  )

  val testTransactor: Transactor[IO] = new DBTransactor(testDBConfig).xa[IO]

}
