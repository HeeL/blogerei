package controllers

import play.api._
import play.api.mvc._

class Auth extends Controller {

  def sign_in = Action {
    Ok(views.html.sign_in())
  }

}
