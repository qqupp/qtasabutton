package queuegarden.config.instances

import queuegarden.config.DBConfig

object DBTestConfig {

  val config = DBConfig("/tmp/testDatabase.db", "", "", 1)

}
