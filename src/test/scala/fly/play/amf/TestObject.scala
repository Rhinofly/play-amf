package fly.play.amf

import scala.beans.BeanProperty

case class TestObject(@BeanProperty var test:String) {
  def this() = this(null)
}