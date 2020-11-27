package queuegarden.routes

import org.http4s.{ Charset, MediaType }
import org.http4s.headers.`Content-Type`

object ContentType {

  val html: `Content-Type` =
    `Content-Type`(MediaType.text.html, Some(Charset.`UTF-8`))

}
