package queuegarden.sql

import cats.effect.Bracket
import doobie._
import doobie.implicits._

trait Migration[F[_]] {
  def migrate: F[Unit]
}

class HomeMadeMigration[F[_]](
    transactor: Transactor[F]
  )(implicit B: Bracket[F, Throwable])
    extends Migration[F] {

  val userTable: doobie.Update0 =
    sql"""create table if not exists user (id integer, name string)""".update

  val migrate: F[Unit] =
    B.flatMap(userTable.run.transact(transactor))(_ => B.pure(()))

}
