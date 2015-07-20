package controllers

import models.User
import tables.UserTable
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

class Auth extends Controller with UserTable {

  lazy val authForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText) verifying ("Invalid user or password", result => result match {
      case (email, password) => {
        authenticate(email, password)
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
