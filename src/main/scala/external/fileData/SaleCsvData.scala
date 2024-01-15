package org.project.scala
package external.fileData

import domain.data.SaleRepository

import com.github.tototoshi.csv.*
import org.project.scala.domain.model.SaleData
import org.project.scala.external.fileData.SaleCsvData.FILE_PATH
import org.project.scala.domain.model.Cities.City
import zio.Config.LocalDateTime
import zio.stream.ZStream

import java.io.File

class SaleCsvData extends SaleRepository{
  override def getSaleData: List[SaleData] = {
    val fileToRead = new File(FILE_PATH)
    val reader = CSVReader.open(fileToRead)
    try {
      reader.allWithHeaders().map { line =>
        SaleData(
          line("ID"),
          line("Vente"),
          City(line("Adresse")),
          line("TypeB"),
          line("Surface").toInt,
          line("Nombre de Chambres").toInt,
          line("Prix").toInt,
          LocalDateTime.parse(line("Date Vente")).getOrElse(null)
        )
      }
    } finally {
      reader.close()
    }
  }
}
object SaleCsvData{
  val FILE_PATH = "src/main/resources/saleBus.csv"
}
