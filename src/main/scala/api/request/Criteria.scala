package org.project.scala
package api.request

import zio.http.Header.IfRange.DateTime
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Criteria(
                       budget: Double,
                       typeB: String,
                       city: List[String],
                       numberOfRooms: NumberOfRoomsInterval,
                       surface: SurfaceInterval,
                       dataSince: LocalDateTime
                     )

object Criteria {
  implicit val encoder: JsonEncoder[Criteria] = DeriveJsonEncoder.gen[Criteria]
  implicit val decoder: JsonDecoder[Criteria] = DeriveJsonDecoder.gen[Criteria]

  def check(criteria: Criteria): ZIO[Any, Throwable, Unit] = {
    for {
      _ <- checkBudget(criteria.budget)
      _ <- checkListOfCity(criteria.city)
      _ <- checkNumberOfRooms(criteria.numberOfRooms)
      _ <- checkSurface(criteria.surface)
    } yield ()
  }

  private def checkBudget(budget: Double): ZIO[Any, Throwable, Unit] = {
    if (budget <= 5000)
      ZIO.fail(new RuntimeException("You must have budget greater than 5000"))
    else
      ZIO.succeed(())
  }

  private def checkListOfCity(cities: List[String]): ZIO[Any, Throwable, Unit] = {
    if (cities.length <= 4 && cities.nonEmpty)
      ZIO.fail(new RuntimeException("Cities list must have more than 4 cities for analysis"))
    else
      ZIO.succeed(())
  }

  private def checkNumberOfRooms(numberOfRooms: NumberOfRoomsInterval): ZIO[Any, Throwable, Unit] = {
    if (numberOfRooms.min < 1 || numberOfRooms.max > 99)
      ZIO.fail(new RuntimeException("Number of rooms must be between 1 & 99"))
    else
      ZIO.succeed(())
  }

  private def checkSurface(surface: SurfaceInterval): ZIO[Any, Throwable, Unit] = {
    if (surface.min < 1 || surface.max > 2000)
      ZIO.fail(new RuntimeException("Surface must be between 1 & 2000"))
    else
      ZIO.succeed(())
  }
}

case class NumberOfRoomsInterval(min: Int, max: Int)

object NumberOfRoomsInterval {
  implicit val encoder: JsonEncoder[NumberOfRoomsInterval] = DeriveJsonEncoder.gen[NumberOfRoomsInterval]
  implicit val decoder: JsonDecoder[NumberOfRoomsInterval] = DeriveJsonDecoder.gen[NumberOfRoomsInterval]
}

case class SurfaceInterval(min: Int, max: Int)

object SurfaceInterval {
  implicit val encoder: JsonEncoder[SurfaceInterval] = DeriveJsonEncoder.gen[SurfaceInterval]
  implicit val decoder: JsonDecoder[SurfaceInterval] = DeriveJsonDecoder.gen[SurfaceInterval]
}

implicit val localDateTimeEncoder: JsonEncoder[LocalDateTime] =
  JsonEncoder[String].contramap(_.toString)
implicit val localDateTimeDecoder: JsonDecoder[LocalDateTime] =
  JsonDecoder[String].map(LocalDateTime.parse)