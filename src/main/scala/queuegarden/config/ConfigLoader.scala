package queuegarden.config
import pureconfig._
import pureconfig.generic.auto._

object ConfigLoader {

  val config: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]

}
