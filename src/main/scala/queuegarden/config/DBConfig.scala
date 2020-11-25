package queuegarden.config


final case class DBConfig(
                           dbPath: String,
                           user: String,
                           password: String) {
  assert(dbPath.nonEmpty, "dbPath can't be empty")
}