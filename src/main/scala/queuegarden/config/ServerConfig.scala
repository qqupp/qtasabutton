package queuegarden.config

final case class ServerConfig(
    port: Int
  ) {
  assert(port > 0 && port < 65535, s"port must be a valid was $port")
}
