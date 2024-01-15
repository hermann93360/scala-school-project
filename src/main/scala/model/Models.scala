package org.project.scala
package model

import org.project.scala.model.Cities.City
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.{LocalDate, LocalDateTime}

case class SaleData(
                     id: String,
                     sale: String,
                     city: City,
                     typeB: String,
                     area: Int,
                     numberOfRooms: Int,
                     price: Int,
                     saleDate: LocalDateTime
                   )

case class TopCities(
                    firstCity: CityStatistics,
                    secondCity: CityStatistics,
                    thirdCity: CityStatistics,
                    )

case class DetailedCity(
                       city: City,
                       comment: String
                       )
object SaleData {
  implicit val encoder: JsonEncoder[SaleData] = DeriveJsonEncoder.gen[SaleData]
}

case class CityStatistic(
                          typeB: String,
                          averagePrice: Double
                        )

case class CityRoomStatistic(
                              numberOfRooms: Int,
                              averagePrice: Double
                            )

case class CityData(
                     averagePrices: List[CityStatistic],
                     salesCounts: Int,
                     roomPriceAnalysis: List[CityRoomStatistic]
                   )

case class CityStatistics(
                           city: City,
                           data: CityData,
                         )

object CityStatistic {
  implicit val encoder: JsonEncoder[CityStatistic] = DeriveJsonEncoder.gen[CityStatistic]
}

object CityRoomStatistic {
  implicit val encoder: JsonEncoder[CityRoomStatistic] = DeriveJsonEncoder.gen[CityRoomStatistic]
}

object CityData {
  implicit val encoder: JsonEncoder[CityData] = DeriveJsonEncoder.gen[CityData]
}

object TopCities {
  implicit val encoder: JsonEncoder[TopCities] = DeriveJsonEncoder.gen[TopCities]
}

object CityStatistics {
  implicit val encoder: JsonEncoder[CityStatistics] = DeriveJsonEncoder.gen[CityStatistics]
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