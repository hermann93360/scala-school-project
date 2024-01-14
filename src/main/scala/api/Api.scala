package org.project.scala
package api

import org.project.scala.model.Cities.City
import org.project.scala.model.{CityStatistics, SaleData}
import org.project.scala.model.CityStatistics.*
import org.project.scala.service.SaleService
import zio.ZIOAppDefault
import zio.http.{App, Http, Request}
import zio.*
import zio.http.*
import zio.json.*

object Api extends ZIOAppDefault {
  
  private val saleService = SaleService.init

  private val static: App[Any] = Http.collectZIO[Request] {
    case Method.GET -> Root / "statistics" =>
      saleService.calculateStatistics()
        .map(_.toJson)
        .map(Response.json)

    case Method.GET -> Root / "statistics" / city =>
      saleService.calculateStatisticsByCity(City(city))
        .map(_.toJson)
        .map(Response.json)
  }.withDefaultErrorResponse
  
  
  private val appLogic: ZIO[Server, Throwable, Unit] = for {
    _ <- Server.serve(static)
  } yield ()
  
  override def run: ZIO[Any, Throwable, Unit]  =
    appLogic.provide(Server.default)
}