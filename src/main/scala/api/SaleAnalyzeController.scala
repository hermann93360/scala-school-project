package org.project.scala
package api

import org.project.scala.api.exception.CriteriaException
import org.project.scala.api.request.Criteria
import org.project.scala.api.response.ErrorResponse
import org.project.scala.domain.service.SaleAnalyzeService
import org.project.scala.domain.model.{CityStatistics, SaleData, TopCities}
import org.project.scala.domain.usecase.SaleAnalyzeUseCase
import org.project.scala.external.fileData.SaleCsvData
import zio.ZIOAppDefault
import zio.http.{App, Http, Request}
import zio.*
import zio.http.*
import zio.json.*

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SaleAnalyzeController(saleAnalyzeUseCase: SaleAnalyzeUseCase) {

  val endpoints: App[Any] = Http.collectZIO[Request] {
    case Method.GET -> Root / "statistics" =>
      saleAnalyzeUseCase.calculateStatistics()
        .map(_.toJson)
        .map(Response.json)

    case req@Method.POST -> Root / "top-cities" =>
      req.body.asString.flatMap { bodyString =>
        ZIO.from(bodyString.fromJson[Criteria])
          .mapError(_ => new RuntimeException("Invalid data format"))
          .flatMap(checkAndGetTopCities)
          .map(_.toJson)
          .map(Response.json)
          .catchAll(handleException)
      }
  }.withDefaultErrorResponse

  private def checkAndGetTopCities(criteria: Criteria): ZIO[Any, Throwable, TopCities] =
    Criteria.check(criteria)
      .flatMap(_ => saleAnalyzeUseCase.findTopCities(criteria))

  private def handleException(e: Throwable): ZIO[Any, Nothing, Response] = e match {
    case e: CriteriaException =>
      ZIO.succeed(Response.json(ErrorResponse(Status.BadRequest.code, e.getMessage).toJson).withStatus(Status.BadRequest))
    case e: ClassNotFoundException =>
      ZIO.succeed(Response.json(ErrorResponse(Status.NotFound.code, e.getMessage).toJson).withStatus(Status.NotFound))
    case _ =>
      ZIO.succeed(Response.json(ErrorResponse(Status.InternalServerError.code, "Internal Server Error").toJson).withStatus(Status.InternalServerError))
  }
}
object SaleAnalyzeController {
  def init: SaleAnalyzeController = {
    SaleAnalyzeController(SaleAnalyzeService.initWith(SaleCsvData()))
  }
}