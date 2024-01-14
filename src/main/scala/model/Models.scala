package org.project.scala
package model

import org.project.scala.model.Cities.City
import zio.json.{DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class SaleData(
                     id: String,
                     sale: String,
                     city: City,
                     typeB: String,
                     area: Int,
                     numberOfRooms: Int,
                     price: Int,
                     saleDate: String
                   )

object SaleData {
  implicit val encoder: JsonEncoder[SaleData] = DeriveJsonEncoder.gen[SaleData]
}

case class CityStatistic(
                          city: String,
                          typeB: String,
                          averagePrice: Double
                        )

object CityStatistic {
  implicit val encoder: JsonEncoder[CityStatistic] = DeriveJsonEncoder.gen[CityStatistic]
}

case class CityRoomStatistic(
                              city: String,
                              numberOfRooms: Int,
                              averagePrice: Double
                            )

object CityRoomStatistic {
  implicit val encoder: JsonEncoder[CityRoomStatistic] = DeriveJsonEncoder.gen[CityRoomStatistic]
}

case class CityStatistics(
                           averagePrices: List[CityStatistic],
                           salesCounts: Map[String, Int],
                           roomPriceAnalysis: List[CityRoomStatistic]
                         )

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