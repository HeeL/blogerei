import controllers.Auth
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.mvc.Results
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import data.TestData


@RunWith(classOf[JUnitRunner])
class AuthSpec extends Specification with Results with Mockito {

  "Auth" should {

    "should be valid/redirected after successful authentication" in new WithApplication {
        val controller = new Auth

        Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

        val request = FakeRequest(POST, "/sign_in").withFormUrlEncodedBody(
          "email" -> "bob1@example.com",
          "password" -> "secret"
        ).withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded")

        val result = controller.signIn()(request)

        status(result) must equalTo(303)
    }

    "should be invalid auth" in new WithApplication {
        val controller = new Auth

        Await.result(controller.dbConfig.db.run(TestData.setup), Duration.Inf)

        val request = FakeRequest(POST, "/sign_in").withFormUrlEncodedBody(
          "email" -> "bob2@example.com",
          "password" -> "secret"
        ).withHeaders(CONTENT_TYPE -> "application/x-www-form-urlencoded");

        val result = controller.signIn()(request)

        status(result) must equalTo(400)
    }
  }
}
