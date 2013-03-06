package fly.play.amf

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._

import Router.queryString

object FakeRoutes extends Router.Routes {

  private var _prefix = "/"

  def setPrefix(prefix: String) {
    _prefix = prefix
    List[(String, Routes)]().foreach {
      case (p, router) => router.setPrefix(prefix + (if (prefix.endsWith("/")) "" else "/") + p)
    }
  }

  def prefix = _prefix

  lazy val defaultPrefix = { if (FakeRoutes.prefix.endsWith("/")) "" else "/" }

  // @LINE:7
  private[this] lazy val testAction =
    Route("POST", PathPattern(List(StaticPart(FakeRoutes.prefix), StaticPart(FakeRoutes.defaultPrefix), StaticPart("testAction"))))

  def documentation = List(("""POST""", prefix + (if (prefix.endsWith("/")) "" else "/") + """testAction""", """TestController.testAction""")).foldLeft(List.empty[(String, String, String)]) { (s, e) =>
    e match {
      case r @ (_, _, _) => s :+ r.asInstanceOf[(String, String, String)]
      case l => s ++ l.asInstanceOf[List[(String, String, String)]]
    }
  }

  def routes: PartialFunction[RequestHeader, Handler] = {

    // @LINE:7
    case testAction(params) => {
      call {
        invokeHandler(TestController.testAction, HandlerDef(this, "TestController.testAction", "testAction", Nil, "POST", """""", FakeRoutes.prefix + """testAction"""))
      }
    }

  }

}
    
