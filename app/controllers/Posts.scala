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
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile


class Posts extends Controller with PostTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  def index = Action.async {
    db.run(posts.result).map(res => Ok(views.html.index(res.toList)))
  }

  def show(id: Int) = Action.async {
    val post = posts.filter(_.id === id)
    db.run(post.result.headOption).map(res => Ok(views.html.show(res.get)))
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

  def newPost = Action {
    Ok(views.html.post_form(postForm))
  }

  def create = Action.async { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => {
        concurrent.Future { BadRequest(views.html.post_form(formWithErrors)) }
      },
      post => {
        db.run(posts += post).map(_ => Redirect(routes.Posts.index))
      }
    )
  }

  def edit(id: Int) = Action.async {
    val post = posts.filter(_.id === id)
    db.run(post.result.headOption).map(res => Ok(views.html.post_form(postForm.fill(res.get), id)))
  }

  def update(id: Int) = Action {
    Redirect(routes.Auth.signInForm())
  }

  def delete(id: Int) = Action.async {
    val post = posts.filter(_.id === id)
    db.run(post.result).map(_ => Redirect(routes.Posts.index))
  }

}
