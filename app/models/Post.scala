package models

case class Post(id: Int, title: String, task: String, solution: String, solution2: Option[String], tests: String)
