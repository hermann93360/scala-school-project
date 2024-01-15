package org.project.scala
package service

import zio.ZIO
import zio.stream.ZStream

import scala.collection.immutable.Map
import com.github.tototoshi.csv.CSVReader
import com.github.tototoshi.csv.defaultCSVFormat
import org.project.scala.api.request.DataFilter
import org.project.scala.data.CsvReader
import org.project.scala.model.Cities.City
import org.project.scala.model.Cities.City.unapply
import org.project.scala.model.{CityData, CityRoomStatistic, CityStatistic, CityStatistics, SaleData, TopCities}
import org.project.scala.service.SaleService.getFile
import zio.*
import zio.Console.{printLine, readLine}
import zio.stream.*

import java.io.{File, StringReader}
import scala.util.{Try, Using}

case class SaleService(){

  val stream: ZStream[Any, Throwable, SaleData] = CsvReader.readSalesData(getFile)

  private def calculateAveragePrice(stream: ZStream[Any, Throwable, SaleData], city: City): ZIO[Any, Throwable, List[CityStatistic]] = {
    stream
      .filter(_.city.equals(city))
      .run(ZSink.foldLeft(Map.empty[String, (Int, Double)]) { (acc, sale) =>
        val (count, total) = acc.getOrElse(sale.typeB, (0, 0.0))
        acc.updated(sale.typeB, (count + 1, total + sale.price))
      })
      .map(_.toList.map { case (typeB, (count, total)) =>
        CityStatistic(typeB, total / count)
      })
  }

  private def countSalesPerCity(stream: ZStream[Any, Throwable, SaleData], city: City): ZIO[Any, Throwable, Int] = {
    stream
      .filter(_.city == city)
      .run(ZSink.foldLeft(0)((count, _) => count + 1))
  }

  private def calculatePriceByNumberOfRooms(stream: ZStream[Any, Throwable, SaleData], city: City): ZIO[Any, Throwable, List[CityRoomStatistic]] = {
    stream
      .filter(_.city.equals(city))
      .run(ZSink.foldLeft(Map.empty[Int, (Int, Double)]) { (acc, sale) =>
        val (count, total) = acc.getOrElse(sale.numberOfRooms, (0, 0.0))
        acc.updated(sale.numberOfRooms, (count + 1, total + sale.price))
      })
      .map(_.toList.map { case (numRooms, (count, total)) =>
        CityRoomStatistic(numRooms, total / count)
      })
  }

  def extractByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, SaleData] = {
    val filteredStream = stream.filter(_.city.equals(city))
    filteredStream.runHead.flatMap {
      case Some(data) => ZIO.succeed(data)
      case None       => ZIO.fail(new NoSuchElementException("No data found for the city"))
    }
  }

  def calculateStatistics(): ZIO[Any, Throwable, List[CityStatistics]] = {
    stream
      .run(ZSink.foldLeft(Map.empty[String, List[SaleData]])((acc, sale) =>
        acc.updated(unapply(sale.city), sale :: acc.getOrElse(unapply(sale.city), List.empty))))
      .flatMap { citySalesMap =>
        ZIO.foreach(citySalesMap.toList) { case (cityName, sales) =>
          for {
            averagePrices <- calculateAveragePrice(ZStream.fromIterable(sales), City(cityName))
            salesCount <- ZIO.succeed(sales.length)
            roomPriceAnalysis <- calculatePriceByNumberOfRooms(ZStream.fromIterable(sales), City(cityName))
          } yield CityStatistics(City(cityName), CityData(averagePrices, salesCount, roomPriceAnalysis))
        }
      }
  }

  def calculateStatisticsByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, CityStatistics] = {
    for {
      averagePrices <- calculateAveragePrice(stream, city)
      salesCounts <- countSalesPerCity(stream, city)
      roomPriceAnalysis <- calculatePriceByNumberOfRooms(stream.filter(_.city == city), city)
    } yield CityStatistics(city, CityData(averagePrices, salesCounts, roomPriceAnalysis))
  }

  def findTopCities(dataFilter: DataFilter): ZIO[Any, Throwable, TopCities] = {

    val filteredStream = stream.filter { sale =>
      (dataFilter.city.isEmpty || dataFilter.city.contains(sale.city)) &&
        sale.numberOfRooms >= dataFilter.numberOfRooms.min &&
        sale.numberOfRooms <= dataFilter.numberOfRooms.max &&
        sale.typeB == dataFilter.typeB &&
        sale.area >= dataFilter.surface.min &&
        sale.area <= dataFilter.surface.max
    }

    filteredStream
      .run(ZSink.foldLeft(Map.empty[City, List[SaleData]])((acc, sale) =>
        acc.updated(sale.city, sale :: acc.getOrElse(sale.city, List.empty))))
      .flatMap { citySalesMap =>
        ZIO.foreach(citySalesMap.toList) { case (city, sales) =>
          calculateStatisticsByCity(city, ZStream.fromIterable(sales))
        }.map(_.flatMap { cityStat =>
            val avgPriceByType = cityStat.data.averagePrices.map(_.averagePrice).sum / cityStat.data.averagePrices.size.max(1)
            val avgPriceByRoom = cityStat.data.roomPriceAnalysis.map(_.averagePrice).sum / cityStat.data.roomPriceAnalysis.size.max(1)
            val combinedAverage = (avgPriceByType + avgPriceByRoom) / 2

            if (combinedAverage <= dataFilter.budget) Some(cityStat) else None
          }.sortBy(_.data.averagePrices.headOption.map(_.averagePrice).getOrElse(Double.MaxValue))
          .take(3))
      }.flatMap {
        case first :: second :: third :: _ => ZIO.succeed(TopCities(first, second, third))
        case _ => ZIO.fail(new RuntimeException("Pas assez de données pour déterminer le top 3 des villes"))
      }
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