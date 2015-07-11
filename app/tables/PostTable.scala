package tables

import models.Post
import slick.driver.PostgresDriver.api._
import slick.driver._

trait PostTable {

  class Posts(tag: Tag) extends Table[Post](tag, "POSTS") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def title = column[String]("TITLE")

    def task = column[String]("TASK")

    def solution = column[String]("SOLUTION")

    def solution2 = column[Option[String]]("SOLUTION2")

    def tests = column[String]("TESTS")

    def * = (id, title, task, solution, solution2, tests) <> ((Post.apply _).tupled, Post.unapply)
  }

  val posts = TableQuery[Posts]

  posts.schema.create

}