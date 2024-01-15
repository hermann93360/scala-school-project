package org.project.scala

import org.project.scala.api.SaleAnalyzeController.endpoints
import zio.ZIOAppDefault
import zio.*
import zio.http.*

object Main extends ZIOAppDefault {

  private val appLogic: ZIO[Server, Throwable, Unit] = for {
    _ <- Server.serve(endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    appLogic.provide(Server.default)

}
