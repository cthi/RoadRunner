package entities

case class Submission(program: String, language: Symbol)

case class SubmissionWithInput(program: String, language: Symbol, input: String)
