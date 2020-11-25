package queuegarden.sql

import cats.effect.Bracket
import doobie._
import doobie.implicits._

class HomeMadeMigration[F[_]](
    transactor: Transactor[F]) {

  val userTable: doobie.Update0 =
    sql"""create table if not exists user (id integer, name string)""".update

  def migrate(implicit B: Bracket[F, Throwable]): F[Unit] =
    B.flatMap(userTable.run.transact(transactor))(_ => B.pure(()))

}
