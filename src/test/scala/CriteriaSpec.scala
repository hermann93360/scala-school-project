package org.project.scala

import org.project.scala.api.request.{Criteria, NumberOfRoomsInterval, SurfaceInterval}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import zio.test.Assertion.{anything, fails, isUnit}
import zio.test.{ZIOSpecDefault, assertZIO}

import java.time.LocalDateTime

object CriteriaSpec extends ZIOSpecDefault {

  override def spec = suite("CriteriaSpec")(
    test("check should succeed for valid criteria") {
      val validCriteria = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(1, 5),
        surface = SurfaceInterval(50, 500),
        dataSince = LocalDateTime.now
      )
      assertZIO(Criteria.check(validCriteria))(isUnit)
    },
    test("check should fail for budget less than or equal to 5000") {
      val invalidCriteria = Criteria(
        budget = 5000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(1, 5),
        surface = SurfaceInterval(50, 500),
        dataSince = LocalDateTime.now
      )
      assertZIO(Criteria.check(invalidCriteria).exit)(fails(anything))
    },
    test("check should fail for list of 4 or fewer cities") {
      val invalidCriteria = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse"),
        numberOfRooms = NumberOfRoomsInterval(1, 5),
        surface = SurfaceInterval(50, 500),
        dataSince = LocalDateTime.now
      )
      assertZIO(Criteria.check(invalidCriteria).exit)(fails(anything))
    },
    test("check should fail for numberOfRooms with min less than 1 or max greater than 99") {
      val invalidCriteriaMin = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(0, 5), // Invalid min
        surface = SurfaceInterval(50, 500),
        dataSince = LocalDateTime.now
      )
      val invalidCriteriaMax = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(1, 100), // Invalid max
        surface = SurfaceInterval(50, 500),
        dataSince = LocalDateTime.now
      )
      assertZIO(Criteria.check(invalidCriteriaMin).exit)(fails(anything))
      assertZIO(Criteria.check(invalidCriteriaMax).exit)(fails(anything))
    },
    test("check should fail for surface with min less than 1 or max greater than 2000") {
      val invalidCriteriaMin = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(1, 5),
        surface = SurfaceInterval(0, 500), // Invalid min
        dataSince = LocalDateTime.now
      )
      val invalidCriteriaMax = Criteria(
        budget = 10000,
        typeB = "House",
        city = List("Paris", "Lyon", "Marseille", "Toulouse", "Nice", "Bordeaux"),
        numberOfRooms = NumberOfRoomsInterval(1, 5),
        surface = SurfaceInterval(50, 2001), // Invalid max
        dataSince = LocalDateTime.now
      )
      assertZIO(Criteria.check(invalidCriteriaMin).exit)(fails(anything))
      assertZIO(Criteria.check(invalidCriteriaMax).exit)(fails(anything))
    }
  )
}
