package Runners

import akka.actor.Actor
import entities.Run

class Ruby extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String) => {
      sender() ! withProgram(program) { fileName =>
        execute(s"ruby $fileName")
      }
    }
  }
}
