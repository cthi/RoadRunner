package Runners

import akka.actor.Actor
import entities.Run

class Java extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String) => {
      sender() ! withProgram(program) { fileName =>
        execute(s"javac $fileName")
      }
    }
  }
}
