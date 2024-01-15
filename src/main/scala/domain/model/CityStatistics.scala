package org.project.scala
package domain.model

import Cities.City
import zio.http.endpoint.EndpointMiddleware.None
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.{LocalDate, LocalDateTime}

case class CityStatistics(
                           city: City,
                           data: CityData,
                         )
case class CityData(
                     averagePrices: List[CityStatistic],
                     salesCounts: Int,
                     roomPriceAnalysis: List[CityRoomStatistic]
                   )
case class CityStatistic(
                          typeB: String,
                          averagePrice: Double
                        )
case class CityRoomStatistic(
                              surface: Double,
                              numberOfRooms: Int,
                              averagePrice: Double
                            )

object CityStatistics {
  implicit val encoder: JsonEncoder[CityStatistics] = DeriveJsonEncoder.gen[CityStatistics]
}
object CityData {
  implicit val encoder: JsonEncoder[CityData] = DeriveJsonEncoder.gen[CityData]
}
object CityStatistic {
  implicit val encoder: JsonEncoder[CityStatistic] = DeriveJsonEncoder.gen[CityStatistic]
}
object CityRoomStatistic {
  implicit val encoder: JsonEncoder[CityRoomStatistic] = DeriveJsonEncoder.gen[CityRoomStatistic]
}




object Cities {

  opaque type City = String

  object City {

    def apply(value: String): City = value

    def unapply(homeTeam: City): String = homeTeam
  }

  given CanEqual[City, City] = CanEqual.derived

  implicit val homeTeamEncoder: JsonEncoder[City] = JsonEncoder.string
  implicit val homeTeamDecoder: JsonDecoder[City] = JsonDecoder.string
}