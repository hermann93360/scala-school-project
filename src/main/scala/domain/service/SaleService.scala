package org.project.scala
package domain.service

import api.request.Criteria
import domain.data.SaleRepository
import external.fileData.SaleCsvData

import org.project.scala.domain.model.Cities.City
import org.project.scala.domain.model.Cities.City.unapply
import com.github.tototoshi.csv.{CSVReader, defaultCSVFormat}
import org.project.scala.domain.model
import org.project.scala.domain.model.{CityData, CityRoomStatistic, CityStatistic, CityStatistics, SaleData, TopCities}
import zio.*
import zio.Console.{printLine, readLine}
import zio.stream.*

import java.io.{File, StringReader}
import scala.collection.immutable.Map
import scala.util.{Try, Using}

case class SaleService(saleRepository: SaleRepository) {

  private val saleDataStream = getStreamOfSaleData(saleRepository.getSaleData)

  private def getStreamOfSaleData(salesData: List[SaleData]): ZStream[Any, Throwable, SaleData] = {
    ZStream.fromIterable(salesData)
  }
  
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
      .run(ZSink.foldLeft(Map.empty[SaleData, (Int, Double)]) { (acc, sale) =>
        val (count, total) = acc.getOrElse(sale, (0, 0.0))
        acc.updated(sale, (count + 1, total + sale.price))
      })
      .map(_.toList.map { case (sale, (count, total)) =>
        CityRoomStatistic(sale.area, sale.numberOfRooms, total / count)
      })
  }

  def extractByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, SaleData] = {
    val filteredStream = stream.filter(_.city.equals(city))
    filteredStream.runHead.flatMap {
      case Some(data) => ZIO.succeed(data)
      case None => ZIO.fail(new NoSuchElementException("No data found for the city"))
    }
  }

  def calculateStatistics(): ZIO[Any, Throwable, List[CityStatistics]] = {
    saleDataStream
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

  private def calculateStatisticsByCity(city: City, stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, CityStatistics] = {
    for {
      averagePrices <- calculateAveragePrice(stream, city)
      salesCounts <- countSalesPerCity(stream, city)
      roomPriceAnalysis <- calculatePriceByNumberOfRooms(stream.filter(_.city == city), city)
    } yield CityStatistics(city, CityData(averagePrices, salesCounts, roomPriceAnalysis))
  }

  private def filterSales(stream: ZStream[Any, Throwable, SaleData], filter: Criteria): ZStream[Any, Throwable, SaleData] =
    stream.filter { sale =>
      filter.city.forall(_.equals(sale.city)) &&
        sale.numberOfRooms >= filter.numberOfRooms.min &&
        sale.numberOfRooms <= filter.numberOfRooms.max &&
        sale.typeB == filter.typeB &&
        sale.area >= filter.surface.min &&
        sale.area <= filter.surface.max
    }
    
  private def groupSalesByCity(stream: ZStream[Any, Throwable, SaleData]): ZIO[Any, Throwable, Map[City, List[SaleData]]] =
    stream.runFold(Map.empty[City, List[SaleData]])((acc, sale) =>
      acc.updated(sale.city, sale :: acc.getOrElse(sale.city, List.empty)))

  private def calculateCityStatistics(city: City, sales: List[SaleData]): ZIO[Any, Throwable, (CityStatistics, Double)] =
    for {
      cityStat <- calculateStatisticsByCity(city, ZStream.fromIterable(sales))
      avgPriceByType = cityStat.data.averagePrices.map(_.averagePrice).sum / cityStat.data.averagePrices.size.max(1)
      avgPriceByRoom = cityStat.data.roomPriceAnalysis.map(_.averagePrice).sum / cityStat.data.roomPriceAnalysis.size.max(1)
    } yield (cityStat, (avgPriceByType + avgPriceByRoom) / 2)

  def findTopCities(dataFilter: Criteria): ZIO[Any, Throwable, TopCities] = {
    val filteredStream = filterSales(saleDataStream, dataFilter)
    for {
      citySalesMap <- groupSalesByCity(filteredStream)
      cityStats <- ZIO.foreach(citySalesMap.toList) { case (city, sales) => calculateCityStatistics(city, sales) }
      topCities = cityStats
        .collect { case (cityStat, combinedAverage) if combinedAverage <= dataFilter.budget => (cityStat, combinedAverage) }
        .sortBy(_._2)
        .take(3)
    } yield model.TopCities(
      topCities.headOption.map(_._1),
      topCities.lift(1).map(_._1),
      topCities.lift(2).map(_._1)
    )
  }

}

object SaleService {
  def init: SaleService = {
    SaleService(SaleCsvData())
  }
}