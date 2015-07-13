package tables

import models.User
import slick.driver.PostgresDriver.api._
import slick.driver._

trait UserTable {

  class Users(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def email = column[String]("email")

    def password = column[String]("password")

    def * = (id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def findUser(email: String, password: String) = users.filter(u => u.email === email && u.password === password)

}