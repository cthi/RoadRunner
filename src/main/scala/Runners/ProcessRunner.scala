package Runners

import java.io._

import scala.sys.process.{ProcessLogger, _}
import scala.util.Random

sealed trait ProgramResult

final case class ProgramSuccess(stdout: String) extends ProgramResult

final case class ProgramFailure(stdout: String) extends ProgramResult

final case class ProgramError(stdout: String, stderr: String, exitCode: Int) extends ProgramResult

trait ProcessRunner {
  def executeWithInput(command: String, input: String) = runProcessWithInput(command, input) match {
    case (stdout, stderr, 0) => ProgramSuccess(stdout)
    case (stdout, stderr, 1) => ProgramFailure(stderr)
    case (stdout, stderr, exitCode) => ProgramError(stdout, stderr, exitCode)
  }

  def withProgram(program: String)(action: String => ProgramResult): ProgramResult = {
    val fileName = Random.alphanumeric.take(20).mkString
    val file = new File(fileName)
    val writer = new BufferedWriter(new FileWriter(file))

    writer.write(program)
    writer.close()

    val result = action(fileName)

    file.delete()

    result
  }

  private def runProcessWithInput(command: String, input: String) = {
    val stdout = new ByteArrayOutputStream
    val stderr = new ByteArrayOutputStream
    val inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"))

    val stdoutWriter = new PrintWriter(stdout)
    val stderrWriter = new PrintWriter(stderr)

    val exitCode = (command #< inputStream) ! ProcessLogger(stdoutWriter.println, stderrWriter.println)

    stdoutWriter.close()
    stderrWriter.close()

    (stdout.toString, stderr.toString, exitCode)
  }
}
