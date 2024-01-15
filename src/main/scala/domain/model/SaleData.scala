package org.project.scala
package domain.model

import domain.model.Cities.City

import zio.json.{DeriveJsonEncoder, JsonEncoder}

import java.time.LocalDateTime

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
object SaleData {
  implicit val encoder: JsonEncoder[SaleData] = DeriveJsonEncoder.gen[SaleData]
}