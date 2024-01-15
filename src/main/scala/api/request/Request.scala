package org.project.scala
package api.request

import zio.http.Header.IfRange.DateTime
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class DataFilter(
                       budget: Double,
                       typeB: String,
                       city: List[String],
                       numberOfRooms: NumberOfRoomsInterval,
                       surface: SurfaceInterval,
                       dataSince: LocalDateTime
                     )

object DataFilter {
  implicit val encoder: JsonEncoder[DataFilter] = DeriveJsonEncoder.gen[DataFilter]
  implicit val decoder: JsonDecoder[DataFilter] = DeriveJsonDecoder.gen[DataFilter]
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