package data

import models.{Post, User}
import slick.driver.PostgresDriver.api._
import tables.UserTable._
import tables.PostTable._

object TestData {

  val setup = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (users.schema ++ posts.schema).create,

    // Insert some suppliers
    users += User(1, "bob1@example.com", "secret"),

    posts += Post(1, "hello world", "scala demo", "scalac", None, None),
    posts += Post(2, "Extract-Transform-Load (ETL)", "The goal is to extract some scrabble scores from a legacy system. As an input you get a Map in such format: \nMap(1 -> Seq(\"APPLE\", \"ARTICHOKE\"), 2 -> Seq(\"BOAT\", \"BALLERINA\"))\nThe result of convertions should be:\nMap(\"apple\" -> 1, \"artichoke\" -> 1, \"boat\" -> 2, \"ballerina\" -> 2)", "object ETL {\n  def transform(old: Map[Int, Seq[String]]) = {\n    old.flatMap { case (k, v) => v.map (_.toLowerCase -> k) }\n  }\n}", None, None),
    posts += Post(3, "hello world2", "scala demo2", "\nobject Main extends App {\n  println(\"hello scala world\")\n}", None, None)
  )

}
