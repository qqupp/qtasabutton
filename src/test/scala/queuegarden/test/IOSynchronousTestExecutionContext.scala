package queuegarden.test

import cats.effect.{ContextShift, IO, Timer}

import scala.concurrent.ExecutionContext

trait IOSynchronousTestExecutionContext {

  object synchronous extends ExecutionContext {
    def execute(runnable: Runnable): Unit     = runnable.run()
    def reportFailure(cause: Throwable): Unit = cause.printStackTrace()
  }

  implicit val ioTestContextShift: ContextShift[IO] =
    IO.contextShift(synchronous)

  implicit val ioTestTimer: Timer[IO] =
    IO.timer(synchronous)

}
