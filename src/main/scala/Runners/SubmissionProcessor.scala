package Runners

import akka.actor.{Actor, PoisonPill, Props}
import entities.{Run, Submission, SubmissionWithInput, Test}

class SubmissionProcessor extends Actor {
  override def receive: Receive = {
    case Submission(program, language) =>
      runSubmission(program, languageFromSymbol(language).asInstanceOf[Class[Actor]])
    case SubmissionWithInput(program, language, input) =>
      testSubmission(program, input, languageFromSymbol(language).asInstanceOf[Class[Actor]])
  }

  def languageFromSymbol(language: Symbol) = {
    language match {
      case 'Python => classOf[Python]
      case 'Ruby => classOf[Ruby]
      case 'Node => classOf[Node]
      case 'Java => classOf[Java]
      case 'Scala => classOf[Scala]
      case 'C => classOf[C]
    }
  }

  def testSubmission(program: String, input: String, language: Class[Actor]) = {
    val runner = context.actorOf(Props(language))

    runner forward Test(program, input)
    runner ! PoisonPill
  }

  def runSubmission(program: String, language: Class[Actor]) = {
    val runner = context.actorOf(Props(language))

    runner forward Run(program)
    runner ! PoisonPill
  }
}
