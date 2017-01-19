package Runners

import akka.actor.Actor
import entities.Run

class Ruby extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program, input) =>
      sender() ! withProgram(program) { fileName =>
        executeWithInput(s"ruby $fileName", input)
      }
  }
}
