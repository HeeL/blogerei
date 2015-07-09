package controllers

import play.api._
import play.api.mvc._

class Posts extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def show(id: Long) = Action {
    Ok(views.html.show())
  }

  def newPost = Action {
    Ok(views.html.post_form())
  }

  def create = Action {
    Redirect(routes.Auth.signInForm())
  }

  def edit(id: Long) = Action {
    Ok(views.html.post_form())
  }

  def update(id: Long) = Action {
    Redirect(routes.Auth.signInForm())
  }

  def delete(id: Long) = Action {
    Redirect(routes.Auth.signInForm())
  }

}
