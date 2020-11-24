package queuegarden.config

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.implicits._

class ConfigLoaderSpec extends AnyFlatSpec with Matchers {

  "config" should "loaded from resources" in {

    val loaded = ConfigLoader.config

    loaded shouldBe AppConfig(ServerConfig(8080))
  }

}
