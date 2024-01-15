package org.project.scala
package domain.model

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class TopCities(
                      firstCity: Option[CityStatistics],
                      secondCity: Option[CityStatistics],
                      thirdCity: Option[CityStatistics],
                    )
object TopCities {
  implicit val encoder: JsonEncoder[TopCities] = DeriveJsonEncoder.gen[TopCities]
}