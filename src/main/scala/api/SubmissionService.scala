package api

import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import entities.Submission
import spray.http.StatusCodes
import spray.json.DefaultJsonProtocol
import spray.routing.Directives

import scala.concurrent.ExecutionContext

import scala.util.{Failure, Success}

object SubmissionService {
  object SubmissionServiceProtocols extends DefaultJsonProtocol {
    implicit val submissionProtocol = jsonFormat2(Submission)
  }
}

class SubmissionService(actor: ActorRef)(implicit ec: ExecutionContext) extends Directives {

  import SubmissionService._
  import SubmissionServiceProtocols._
  import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  val route =
    path("") {
      get {
        complete("Pong")
      }
    } ~
      path("submit") {
        post {
          entity(as[Submission]) { submission =>
            onComplete(actor ? submission) {
              case Success(_) => complete(StatusCodes.OK)
              case Failure(_) => complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
}
