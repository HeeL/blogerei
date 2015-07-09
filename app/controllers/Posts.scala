package controllers

import play.api._
import play.api.mvc._

class Posts extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def show(id: Int) = Action {
    Ok(views.html.show())
  }

}
