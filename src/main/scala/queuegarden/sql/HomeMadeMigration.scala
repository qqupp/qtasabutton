package queuegarden.sql

import cats.effect.Bracket
import doobie._
import doobie.implicits._
import cats.implicits._

class HomeMadeMigration[F[_]](
    transactor: Transactor[F]) {

  val userTable: doobie.Update0 =
    sql"""CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, name VARCHAR)""".update

  val insertAdmin: doobie.Update0 =
    sql"""INSERT OR IGNORE INTO user VALUES (0, 'admin')""".update

  def migrate(implicit B: Bracket[F, Throwable]): F[Unit] =
    B.flatMap((userTable.run *> insertAdmin.run).transact(transactor))(_ =>
      B.pure(())
    )

}
