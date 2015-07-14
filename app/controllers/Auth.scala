package controllers

import models.User
import tables.UserTable
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Auth extends Controller with UserTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  lazy val authForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText) verifying ("Invalid user or password", result => result match {
      case (email, password) => {
        Await.result(db.run(authenticate(email, password)), Duration.Inf) == 1
      }
      case _ => false
    })
  )


  def signInForm = Action {
    Ok(views.html.sign_in_form(authForm))
  }

  def signIn = Action { implicit request =>
    authForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.sign_in_form(formWithErrors)),
      user => Redirect(routes.Posts.index).withSession("email" -> user._1)
    )
  }

}
