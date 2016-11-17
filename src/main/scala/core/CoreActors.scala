package core

import java.util.concurrent.TimeUnit

import Runners.SubmissionProcessor
import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import api.Api
import spray.can.Http

trait CoreActors {
  this: Core =>

  val submissionHandler = actorSystem.actorOf(Props[SubmissionProcessor], "SubmissionRunner")
  val api = actorSystem.actorOf(Props(classOf[Api], submissionHandler), "ApiService")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  IO(Http) ? Http.Bind(api, interface = "localhost", port = 8080)
}
