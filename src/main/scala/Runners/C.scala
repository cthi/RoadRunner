package Runners

import akka.actor.Actor
import entities.Run

class C extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String) =>
      withProgram(program) { filename =>
        val commands = List(
          s"g++ $filename.c -o $filename",
          s"./$filename"
        )
        execute("./")
      }
  }
}
