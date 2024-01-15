package org.project.scala
package domain.data

import org.project.scala.domain.model.SaleData

trait SaleRepository {
  def getSaleData: List[SaleData]
}
