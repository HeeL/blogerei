package tables

import models.User
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object UserTable extends UserTable

trait UserTable extends HasDatabaseConfig[JdbcProfile]{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  class Users(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def email = column[String]("email")

    def password = column[String]("password")

    def * = (id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def getByEmailAndPassword(email: String, password: String) = {
    users.filter(u => u.email === email && u.password === password)
  }

  def authenticate(email: String, password: String) = {
    Await.result(db.run(getByEmailAndPassword(email, password).length.result), Duration.Inf) == 1
  }

}