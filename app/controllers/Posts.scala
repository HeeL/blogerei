package controllers

import models._
import tables._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc.BodyParsers._
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class Posts extends Controller with Secured with PostTable {
  import driver.api._

  def index = Action.async {
    getPosts.map(res => Ok(views.html.index(res.toList)))
  }

  def show(id: Int) = Action.async {
    getPost(id).map(res => Ok(views.html.show(res.get)))
  }

  val postForm = Form(
    mapping(
      "id" -> ignored(1),
      "title" -> nonEmptyText,
      "task" -> nonEmptyText,
      "solution" -> nonEmptyText,
      "solution2" -> optional(text),
      "tests" -> optional(text)
    )(Post.apply)(Post.unapply)
  )

  def newPost = (UserAction andThen isAuthenticatedFilter) {
    implicit request =>
    Ok(views.html.post_form(postForm))
  }

  def create = (UserAction andThen isAuthenticatedFilter).async { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => {
        concurrent.Future { BadRequest(views.html.post_form(formWithErrors)) }
      },
      post => {
        createPost(post).map(_ => Redirect(routes.Posts.index))
      }
    )
  }

  def edit(id: Int) = (UserAction andThen isAuthenticatedFilter).async {
    getPost(id).map(res => Ok(views.html.post_form(postForm.fill(res.get), id)))
  }

  def update(id: Int) = (UserAction andThen isAuthenticatedFilter).async { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => {
        concurrent.Future { BadRequest(views.html.post_form(formWithErrors, id)) }
      },
      post => {
        updatePost(id, post).map(_ => Redirect(routes.Posts.index))
      }
    )
  }

  def delete(id: Int) = (UserAction andThen isAuthenticatedFilter).async {
    deletePost(id).map(_ => Redirect(routes.Posts.index))
  }

}
