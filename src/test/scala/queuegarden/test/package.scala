package queuegarden

import org.scalatest.Assertions

package object test {
  val pureTest                      = IOPureTest
  def pendingTest[T](t: => T): Unit = (Assertions.pending)
}
