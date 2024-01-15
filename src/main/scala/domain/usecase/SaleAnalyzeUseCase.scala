package org.project.scala
package domain.usecase

import api.request.Criteria

import org.project.scala.domain.model.{CityStatistics, TopCities}
import zio.ZIO

trait SaleAnalyzeUseCase {
  def findTopCities(criteria: Criteria): ZIO[Any, Throwable, TopCities]
  def calculateStatistics(): ZIO[Any, Throwable, List[CityStatistics]]
}
