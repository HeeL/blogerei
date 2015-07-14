package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def about = Action {
    Ok(views.html.about())
  }

}

/**
 * Provide security features
 */
trait Secured {
  self: Controller =>

  def username(request: RequestHeader) = request.session.get("email")

  def onUnauthorized(request: RequestHeader): Result

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
}
