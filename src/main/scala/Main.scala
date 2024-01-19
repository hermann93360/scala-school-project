package org.project.scala

import org.project.scala.api.SaleAnalyzeController
import org.project.scala.domain.service.SaleAnalyzeService
import org.project.scala.external.fileData.SaleCsvData
import zio.ZIOAppDefault
import zio.*
import zio.http.*

object Main extends ZIOAppDefault {

  private val appLogic: ZIO[Server, Throwable, Unit] = for {
    _ <- Server.serve(getSaleAnalyzeControllerWithDependency.endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    appLogic.provide(Server.default)

  private def getSaleAnalyzeControllerWithDependency: SaleAnalyzeController = {
    val saleRepository = SaleCsvData()
    val saleAnalyseUseCase = SaleAnalyzeService.initWith(saleRepository);
    val saleAnalyzeController = SaleAnalyzeController.initWith(saleAnalyseUseCase)
  }
}
