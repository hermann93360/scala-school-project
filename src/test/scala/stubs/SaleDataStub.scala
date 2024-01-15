package org.project.scala
package stubs

import domain.data.SaleRepository

import org.project.scala.domain.model.Cities.City
import org.project.scala.domain.model.SaleData
import org.project.scala.stubs.SaleDataStub.persistData

import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer

class SaleDataStub extends SaleRepository {

  override def getSaleData: List[SaleData] = {
    persistData.toList
  }
}

object SaleDataStub {

  private val persistData: ListBuffer[SaleData] = ListBuffer[SaleData]()

  def createSaleDateAndAdd(id: String, sale: String, city: City, typeB: String, area: Int, numberOfRooms: Int, price: Int, saleDate: LocalDateTime): Unit = {
    val data = SaleData(id, sale, city, typeB, area, numberOfRooms, price, saleDate)
    persistData += data
  }
}
