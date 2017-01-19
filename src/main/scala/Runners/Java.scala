package Runners

import akka.actor.Actor
import entities.Run

class Java extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String, input: String) =>
      sender() ! withProgram(program) { fileName =>
        executeWithInput(s"javac $fileName", input)
      }
  }
}
