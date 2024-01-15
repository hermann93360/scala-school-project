package org.project.scala

import org.project.scala.api.request.{Criteria, NumberOfRoomsInterval, SurfaceInterval}
import org.project.scala.domain.model.Cities.City
import org.project.scala.domain.model.TopCities
import org.project.scala.domain.service.SaleAnalyzeService
import org.project.scala.domain.usecase.SaleAnalyzeUseCase
import org.project.scala.stubs.SaleDataStub
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.{should, shouldBe}
import zio.ZIO
import zio.schema.validation.Regex.Digit.test
import zio.test.{ZIOSpecDefault, assertCompletes}

import java.time.LocalDateTime

object SaleAnalyzeUseCaseSpec extends ZIOSpecDefault with Matchers {

  val tested: SaleAnalyzeUseCase = SaleAnalyzeService.initWith(SaleDataStub())
  override def spec = suite("SaleServiceSpec")(
    test("should return a list of cities for valid criteria") {
      for {
        _ <- ZIO.attempt {
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("A"), "Villa", 300, 5, 200000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("C"), "Villa", 300, 4, 100000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("C"), "Villa", 300, 4, 90000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("A"), "Villa", 300, 3, 150000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Villa", 300, 5, 250000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Villa", 300, 5, 400000, LocalDateTime.now)
          SaleDataStub.createSaleDateAndAdd("S1", "Avenue de la Liberté 45", City("B"), "Maison", 300, 5, 200000, LocalDateTime.now)
        }
        criteria = Criteria(500000, "Villa", List.empty, NumberOfRoomsInterval(1, 6), SurfaceInterval(20, 600), LocalDateTime.now)
        result <- tested.findTopCities(criteria)
        _ <- ZIO.succeed {
          result shouldBe a[TopCities]
          result.firstCity should not be None
          result.secondCity should not be None
          result.thirdCity should not be None
          result.firstCity.get.city should be("C")
          result.secondCity.get.city should be("A")
          result.thirdCity.get.city should be("B")
        }
      } yield assertCompletes
    }
  )
  
}
