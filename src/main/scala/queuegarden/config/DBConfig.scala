package queuegarden.config

final case class DBConfig(
    dbPath: String,
    user: String,
    password: String,
    queryConcurrencyLevel: Int) {
  assert(dbPath.nonEmpty, "dbPath can't be empty")
  assert(queryConcurrencyLevel > 0, "queryConcurrencyLevel must be positive")
}
