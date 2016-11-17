package Runners

import akka.actor.Actor
import entities.Run

class Java extends Actor {
  override def receive: Receive = {
    case Run(program: String) =>
  }
}
