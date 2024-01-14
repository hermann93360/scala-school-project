package org.project.scala
package service

import zio.ZIO
import zio.stream.ZStream

import scala.collection.immutable.Map
import com.github.tototoshi.csv.CSVReader
import com.github.tototoshi.csv.defaultCSVFormat
import org.project.scala.data.CsvReader
import org.project.scala.model.Cities.City
import org.project.scala.model.Cities.City.unapply
import org.project.scala.model.{CityRoomStatistic, CityStatistic, CityStatistics, SaleData}
import org.project.scala.service.SaleService.getFile
import zio.*
import zio.Console.{printLine, readLine}
import zio.stream.*

import java.io.{File, StringReader}
import scala.util.{Try, Using}

case class SaleService(){

  val stream: ZStream[Any, Throwable, SaleData] = CsvReader.readSalesData(getFile)

  private def calculateAveragePrice(stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, List[CityStatistic]] = {
    stream
      .run(ZSink.foldLeft(Map.empty[(String, String), (Int, Int)]) { (acc, sale) =>
        val key = (unapply(sale.city), sale.typeB)
        val (totalPrice, count) = acc.getOrElse(key, (0, 0))
        acc.updated(key, (totalPrice + sale.price, count + 1))
      })
      .map(_.view.mapValues { case (totalPrice, count) => totalPrice.toDouble / count }
        .map { case ((city, typeB), avgPrice) => CityStatistic(city, typeB, avgPrice) }.toList)
  }

  private def countSalesPerCity(stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, Map[String, Int]] = {
    stream
      .run(ZSink.foldLeft(Map.empty[String, Int]) { (acc, sale) =>
        acc.updated(unapply(sale.city), acc.getOrElse(unapply(sale.city), 0) + 1)
      })
  }

  private def calculatePriceByNumberOfRooms(stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, List[CityRoomStatistic]] = {
    stream
      .run(ZSink.foldLeft(Map.empty[String, Map[Int, (Int, Int)]]) { (acc, sale) =>
        val cityData = acc.getOrElse(unapply(sale.city), Map.empty[Int, (Int, Int)])
        val (totalPrice, count) = cityData.getOrElse(sale.numberOfRooms, (0, 0))
        acc.updated(unapply(sale.city), cityData.updated(sale.numberOfRooms, (totalPrice + sale.price, count + 1)))
      })
      .map(_.view.mapValues(_.view.mapValues { case (totalPrice, count) => totalPrice.toDouble / count }
          .map { case (numRooms, avgPrice) => CityRoomStatistic(_, numRooms, avgPrice) }.toList)
        .flatMap { case (city, roomStats) => roomStats.map(stat => stat(city)) }.toList)
  }
  
  def extractByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, SaleData] = {
    val filteredStream = stream.filter(_.city.equals(city))
    filteredStream.runHead.flatMap {
      case Some(data) => ZIO.succeed(data)
      case None       => ZIO.fail(new NoSuchElementException("No data found for the city"))
    }
  }

  def calculateStatisticsByCity(city: City): ZIO[Any, Throwable, CityStatistics] = {
    for {
      groupBy <- extractByCity(city, stream)
      averagePrices <- calculateAveragePrice(ZStream.from(groupBy))
      salesCounts <- countSalesPerCity(ZStream.from(groupBy))
      roomPriceAnalysis <- calculatePriceByNumberOfRooms(ZStream.from(groupBy))
    } yield CityStatistics(averagePrices, salesCounts, roomPriceAnalysis)
  }
  
  def calculateStatistics(): ZIO[Any, Throwable, CityStatistics] = {
    for {
      averagePrices <- calculateAveragePrice(stream)
      salesCounts <- countSalesPerCity(stream)
      roomPriceAnalysis <- calculatePriceByNumberOfRooms(stream)
    } yield CityStatistics(averagePrices, salesCounts, roomPriceAnalysis)
  }

}

object SaleService{
  
  private val filePathForAnalyze = "src/main/resources/venteBis.csv"
  
  def getFile: File = {
    File(filePathForAnalyze)
  }
  def init: SaleService = {
    SaleService()
  }
}