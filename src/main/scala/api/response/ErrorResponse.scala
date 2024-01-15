package org.project.scala
package api.response

import zio.json.{DeriveJsonEncoder, JsonEncoder}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

case class ErrorResponse(codeErrorHttp: Int, message: String, date: String)

object ErrorResponse {
  def apply(codeErrorHttp: Int, message: String): ErrorResponse = {
    new ErrorResponse(codeErrorHttp, message, ZonedDateTime.now.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
  }

  implicit val encoder: JsonEncoder[ErrorResponse] = DeriveJsonEncoder.gen[ErrorResponse]
}