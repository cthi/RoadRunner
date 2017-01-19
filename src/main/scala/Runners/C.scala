package Runners

import akka.actor.Actor
import entities.Run

class C extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String, input: String) =>
      withProgram(program) { filename =>
        val commands = List(
          s"g++ $filename.c -o $filename",
          s"./$filename"
        )
        executeWithInput("./", input)
      }
  }
}
