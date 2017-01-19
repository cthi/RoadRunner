package Runners

import akka.actor.Actor
import entities.Run

class Scala extends Actor {
  override def receive: Receive = {
    case Run(program, input) =>
  }
}
