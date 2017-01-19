package Runners

import akka.actor.Actor
import entities.{Run, Test}

class Ruby extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program) =>
      sender() ! withProgram(program) { fileName =>
        execute(s"ruby $fileName")
      }
    case Test(program, input) =>
      sender() ! withProgram(program) { fileName =>
        executeWithInput(s"ruby $fileName", input)
      }
  }
}
