package Runners

import akka.actor.Actor
import entities.{Run, Test}

class Python extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program) =>
      sender() ! withProgram(program) { fileName =>
        execute(s"python3 $fileName")
      }
    case Test(program, input) =>
      sender() ! withProgram(program) { fileName =>
        executeWithInput(s"python3 $fileName", input)
      }
  }
}
