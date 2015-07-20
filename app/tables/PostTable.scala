package tables

import models.Post
import scala.concurrent.Future
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._


trait PostTable extends HasDatabaseConfig[JdbcProfile]{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  class Posts(tag: Tag) extends Table[Post](tag, "posts") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def task = column[String]("task")

    def solution = column[String]("solution")

    def solution2 = column[Option[String]]("solution2")

    def tests = column[Option[String]]("tests")

    def * = (id, title, task, solution, solution2, tests) <> ((Post.apply _).tupled, Post.unapply)
  }

  val posts = TableQuery[Posts]

  def getPosts = db.run(posts.sortBy(_.id.desc).result)

  def getPost(id: Int) = db.run(findPost(id).result.headOption)

  def updatePost(post_id: Int, post: Post) = db.run(findPost(post_id).update(post.copy(id = post_id)))

  def createPost(post: Post) = db.run(posts += post)

  def deletePost(id: Int) = db.run(findPost(id).delete)

  private def findPost(id: Int) = posts.filter(_.id === id)


}