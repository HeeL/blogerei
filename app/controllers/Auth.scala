package controllers

import play.api._
import play.api.mvc._

class Auth extends Controller {

  def signInForm = Action {
    Ok(views.html.sign_in_form())
  }

  def signIn = Action {
    if (true)
      Redirect(routes.Posts.index())
    else
      Redirect(routes.Auth.signInForm())
  }

}
