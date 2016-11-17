package Runners

import akka.actor.Actor
import entities.Run

class Python extends Actor with ProcessRunner {
  override def receive: Receive = {
    case Run(program: String) => {
      withProgram(program) { fileName =>
        execute(s"python3 $fileName") match {
          case ProgramSuccess(stdout) =>
          case ProgramFailure(stdout) =>
          case ProgramError(stdout, stderr, exitCode) =>
        }
      }
    }
  }
}
