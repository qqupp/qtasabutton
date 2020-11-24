package queuegarden.config

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.implicits._

class ServerConfigSpec extends AnyFlatSpec with Matchers {

  "config" should "loaded from resources" in {
    import pureconfig._
    import pureconfig.generic.auto._

    val loaded = ConfigSource.default.load[AppConfig]

    loaded.isRight shouldBe true
    loaded shouldBe AppConfig(ServerConfig(8080)).asRight
  }

}
