package core

import akka.actor.ActorSystem

trait Core {
  protected implicit def actorSystem: ActorSystem
}

trait BootedCore extends Core {
  def actorSystemName: String
  override def actorSystem = ActorSystem(actorSystemName)

  sys.addShutdownHook(actorSystem.terminate())
}
