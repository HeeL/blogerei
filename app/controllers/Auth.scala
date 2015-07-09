package controllers

import play.api._
import play.api.mvc._

class Auth extends Controller {

  def sign_in_form = Action {
    Ok(views.html.sign_in_form())
  }

  def sign_in = Action {
    if (true)
      Redirect(routes.Posts.index())
    else
      Redirect(routes.Auth.sign_in_form())
  }

}
