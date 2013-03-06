package fly.play.amf

import play.api.mvc.Controller
import play.api.mvc.Action

object TestController extends Controller with Amf {
	def testAction = Action(amfParser[TestObject]) { request =>
	  Ok(request.body.test)
	}
}