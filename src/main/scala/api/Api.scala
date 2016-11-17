package api

import akka.actor.{ActorLogging, ActorRef}
import spray.routing.HttpServiceActor

class Api(submissionRunner: ActorRef) extends HttpServiceActor with ActorLogging {
  override val actorRefFactory = context
  implicit val ec = context.dispatcher

  val route = new SubmissionService(submissionRunner).route

  def receive = runRoute(route)
}