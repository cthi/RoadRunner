package api

import java.util.concurrent.TimeUnit

import Runners.{ProgramError, ProgramFailure, ProgramSuccess}
import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import entities.{Submission, SubmissionWithInput}
import spray.http.StatusCodes
import spray.json.DefaultJsonProtocol
import spray.routing.Directives

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object SubmissionService {

  object SubmissionServiceProtocols extends DefaultJsonProtocol {
    implicit val submissionProtocol = jsonFormat2(Submission)
    implicit val submissionWithInputProtocol = jsonFormat3(SubmissionWithInput)
  }

}

class SubmissionService(actor: ActorRef)(implicit ec: ExecutionContext) extends Directives {

  import SubmissionService._
  import SubmissionServiceProtocols._
  import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

  implicit val timeout = Timeout(3, TimeUnit.SECONDS)

  val route =
    path("submit") {
      post {
        entity(as[Submission]) { submission =>
          onComplete(actor ? submission) {
            case Success(programResult) =>
              programResult match {
                case ProgramSuccess(stdout) => complete(StatusCodes.OK, stdout)
                case ProgramFailure(stdout) => complete(StatusCodes.OK, stdout)
                case ProgramError(stdout, stderr, exitCode) => complete(StatusCodes.OK, stderr)
              }
            case Failure(_) => complete(StatusCodes.InternalServerError)
          }
        }
      }
    } ~
    path("run") {
      post {
        entity(as[SubmissionWithInput]) { submission =>
          onComplete(actor ? submission) {
            case Success(programResult) =>
              programResult match {
                case ProgramSuccess(stdout) => complete(StatusCodes.OK, stdout)
                case ProgramFailure(stdout) => complete(StatusCodes.OK, stdout)
                case ProgramError(stdout, stderr, exitCode) => complete(StatusCodes.OK, stderr)
              }
            case Failure(_) => complete(StatusCodes.InternalServerError)
          }
        }
      }
    }
}
