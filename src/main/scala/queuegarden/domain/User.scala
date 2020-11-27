package queuegarden.domain

sealed trait User {
  def id: Int
  def name: String
}

final case class NormalUser(
    id: Int,
    name: String
  ) extends User

final case class AdminUser(
    id: Int,
    name: String
  ) extends User
