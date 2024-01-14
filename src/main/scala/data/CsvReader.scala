package org.project.scala
package data

import zio.stream.ZStream
import com.github.tototoshi.csv.*
import org.project.scala.model.Cities.City
import org.project.scala.model.SaleData
import zio.ZIO

import java.io.File

object CsvReader {
  
  def readSalesData(file: File): ZStream[Any, Throwable, SaleData] = {
    ZStream
      .acquireReleaseWith(ZIO.attempt(CSVReader.open(file)))(reader => ZIO.succeed(reader.close()))
      .flatMap { reader =>
        ZStream.fromIterable(reader.allWithHeaders()).map { line =>
          SaleData(
            line("ID"),
            line("Vente"),
            City(line("Adresse")),
            line("TypeB"),
            line("Surface").toInt,
            line("Nombre de Chambres").toInt,
            line("Prix").toInt,
            line("Date Vente")
          )
        }
      }
  }
}