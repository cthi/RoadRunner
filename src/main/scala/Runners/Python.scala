package Runners

import akka.actor.Actor
import entities.Run

class Python extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String) => {
      sender() ! withProgram(program) { fileName =>
        execute(s"python3 $fileName")
      }
    }
  }
}
