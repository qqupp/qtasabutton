package queuegarden.sql

import cats.effect.IO
import doobie.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import queuegarden.config.instances.DBTestConfig
import queuegarden.test._

class DBTransactorSpec
    extends AnyFlatSpec
    with Matchers
    with IOSynchronousTestExecutionContext {

  "db transactor" should "open a db connection with sqllite and perform some queries" in pureTest {
    val transactor = new DBTransactor(DBTestConfig.config).xa[IO]

    val dbquery = for {
      _       <- dropTable
      _       <- createTable
      i1      <- insert(1, "leo")
      i2      <- insert(2, "yui")
      persons <- selectStar
    } yield TestData(i1 + i2, persons)

    dbquery
      .transact(transactor)
      .flatMap(td =>
        IO {
          td.insertedRows shouldBe 2
          td.persons should contain theSameElementsAs List(
            TestPerson(1, "leo"),
            TestPerson(2, "yui")
          )
        }
      )
  }

  case class TestData(
      insertedRows: Int,
      persons: List[TestPerson]
    )
  case class TestPerson(
      id: Int,
      name: String
    )

  val dropTable: doobie.ConnectionIO[Int] =
    sql"""
      drop table if exists person
       """.update.run

  val createTable: doobie.ConnectionIO[Int] =
    sql"""
      create table person (id integer, name string)
    """.update.run

  def insert(
      id: Int,
      name: String
    ): doobie.ConnectionIO[Int] =
    sql"""
        insert into person values($id, $name)
       """.update.run

  def selectStar: doobie.ConnectionIO[List[TestPerson]] =
    sql"""
         select * from person
       """.query[TestPerson].to[List]

}
