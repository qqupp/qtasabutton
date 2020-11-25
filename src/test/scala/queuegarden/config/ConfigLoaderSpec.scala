package queuegarden.config

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConfigLoaderSpec extends AnyFlatSpec with Matchers {

  "config" should "loaded from resources succesfully" in {
    noException should be thrownBy (ConfigLoader.config)
  }

}
