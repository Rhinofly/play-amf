package fly.play.amf

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Controller
import play.api.mvc.Action
import java.io.ByteArrayOutputStream
import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer
import play.api.libs.iteratee.Enumerator
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.http.Writeable

object AmfSpec extends Specification {

  "Amf" should {

    "be able to handle amf request with simple string" in {

      val test = "test"

      val ba = new ByteArrayOutputStream
      val serializer = new AMF3Serializer(ba)
      serializer writeObject test

      val fakeRequest =
        FakeRequest(POST, "/")
          .withHeaders(CONTENT_TYPE -> "application/x-amf")

      val result = TestController.amfParser[String](fakeRequest)
      val appliedResult = Enumerator(ba.toByteArray) |>>> result
      val actualResult = Await.result(appliedResult, Duration.Inf)

      actualResult must beLike {
        case Right(result) => result must_== test
      }
    }

    "be able to handle amf request with case class" in {

      val test = TestObject("test")

      val ba = new ByteArrayOutputStream
      val serializer = new AMF3Serializer(ba)
      serializer writeObject test

      val fakeRequest =
        FakeRequest(POST, "/")
          .withHeaders(CONTENT_TYPE -> "application/x-amf")

      val result = TestController.amfParser[TestObject](fakeRequest)
      val appliedResult = Enumerator(ba.toByteArray) |>>> result
      val actualResult = Await.result(appliedResult, Duration.Inf)

      actualResult must beLike {
        case Right(result) => result must_== test
      }
    }

    "work as a body parser of an action" in {

      val test = TestObject("test")
      val ba = new ByteArrayOutputStream
      val serializer = new AMF3Serializer(ba)
      serializer writeObject test

      // we need to supply the writable here because the route method uses it's content type
      implicit val wBytes = new Writeable[Array[Byte]](x => x, Some("application/x-amf"))

      val fakeRequest =
        FakeRequest(POST, "/testAction")
          .withBody(ba.toByteArray)

      running(FakeApplication(withRoutes = {
        case ("POST", "/testAction") => TestController.testAction
      })) {
        val result = route(fakeRequest)

        result must beLike {
          case Some(result) => {
            status(result) must equalTo(OK)
          }
        }
      }
    }
  }
}