package tables

import models.Post
import slick.driver.PostgresDriver.api._
import slick.driver._

trait PostTable {

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

}