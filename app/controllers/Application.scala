package controllers

import play.api._
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._

import scala.concurrent.Future

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

  class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

  object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
    def transform[A](request: Request[A]) = Future.successful {
      new UserRequest(request.session.get("email"), request)
    }
  }

  object isAuthenticatedFilter extends ActionFilter[UserRequest] {
    def filter[A](input: UserRequest[A]) = Future.successful {
      input.username match {
        case Some(name) => None
        case None => Some(Redirect(routes.Auth.signInForm()))
      }
    }
  }


}
