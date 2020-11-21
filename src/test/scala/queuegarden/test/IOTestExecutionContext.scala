package queuegarden.test

import cats.effect.{ContextShift, IO, Timer}

trait IOTestExecutionContext {

  implicit val ioTestContextShift: ContextShift[IO] =
    IO.contextShift(scala.concurrent.ExecutionContext.Implicits.global)

  implicit val ioTestTimer: Timer[IO] =
    IO.timer(scala.concurrent.ExecutionContext.Implicits.global)

}
