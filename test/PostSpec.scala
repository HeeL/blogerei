import controllers.Posts
import data.TestData
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.mvc.Results
import play.api.test.Helpers._
import play.api.test._
import util.CustomMatchers._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class PostSpec extends Specification with Results with Mockito {

  "Posts" should {

    "should show all posts" in new WithApplication {
      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(GET, "/")

      val result = controller.index()(request)

      status(result) must equalTo(200)

      contentType(result) must beSome.which(_ == "text/html")

      contentAsString(result) must containAllSubstringsIn(List("Extract-Transform-Load (ETL)", "hello world", "hello world2"))

    }

    "should show post with id 2" in new WithApplication {
      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      //val Some(result) = route(FakeRequest(GET, "/posts/2"))
      val request = FakeRequest(GET, "/posts/2")

      val result = controller.show(2)(request)

      status(result) must equalTo(200)

      contentType(result) must beSome.which(_ == "text/html")

      contentAsString(result) must contain("Map(1 -> Seq(\"APPLE\", \"ARTICHOKE\")")

    }

    "should open form for new post" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(GET, "/posts/new").withSession("email" -> "bob1@example.com")

      val result = controller.newPost()(request)

      status(result) must equalTo(200)
    }

    "should create new post" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(POST, "/posts").withFormUrlEncodedBody(
        "id" -> "0",
        "title" -> "hello scala world new",
        "task" -> "new scala task",
        "solution" -> "some solution",
        "solution2" -> "",
        "tests" -> ""
      ).withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded").withSession("email" -> "bob1@example.com")

      val result = controller.create()(request)

      //check redirection to index page
      status(result) must equalTo(303)

      controller.getPosts.map { posts =>
        posts.size must equalTo(4)
      }

    }

    "should show badrequest on invalid post form" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(POST, "/posts")
        .withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")
        .withSession("email" -> "bob1@example.com")

      val result = controller.create()(request)

      //badrequest is thrown
      status(result) must equalTo(400)

    }

    "should redirect to login page for anonymous user for new post" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(GET, "/posts/new")

      val result = controller.newPost(request)

      status(result) must equalTo(303)

    }

    "should show update post with id 1" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(POST, "/posts/update/1")
        .withFormUrlEncodedBody(
          "id" -> "1",
          "title" -> "new title",
          "task" -> "new scala task",
          "solution" -> "some solution",
          "solution2" -> "",
          "tests" -> ""
        )
        .withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")
        .withSession("email" -> "bob1@example.com")

      val result = controller.update(1)(request)

      status(result) must equalTo(303)

      controller.getPost(1).map { post =>
        post.get.title must equalTo("new title")
      }

    }

    "should show edit form for post with id 2" in new WithApplication {
      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(GET, "/posts/2/edit")
        .withSession("email" -> "bob1@example.com")

      val result = controller.edit(2)(request)

      status(result) must equalTo(200)

      contentType(result) must beSome.which(_ == "text/html")

      contentAsString(result) must contain("Extract-Transform-Load (ETL)")

    }

    "should delete post with id 1" in new WithApplication {

      val controller = new Posts

      Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

      val request = FakeRequest(GET, "/posts/1/delete")
        .withSession("email" -> "bob1@example.com")

      val result = controller.delete(1)(request)

      //redirect to index page after successful delete
      status(result) must equalTo(303)

      controller.getPost(1).map { post =>
        post must equalTo(None)
      }
    }

  }

}
