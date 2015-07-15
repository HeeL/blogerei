import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beSome.which (status(_) == NOT_FOUND)
    }

    "render the about page" in new WithApplication{
      val about = route(FakeRequest(GET, "/about")).get

      status(about) must equalTo(OK)
      contentType(about) must beSome.which(_ == "text/html")
      contentAsString(about) must contain ("What Blogerei Is About")
    }
  }
}
