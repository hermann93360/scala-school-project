package org.project.scala

import org.project.scala.api.SaleAnalyzeController
import zio.ZIOAppDefault
import zio.*
import zio.http.*

object Main extends ZIOAppDefault {

  private val appLogic: ZIO[Server, Throwable, Unit] = for {
    _ <- Server.serve(SaleAnalyzeController.init.endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    appLogic.provide(Server.default)

}
