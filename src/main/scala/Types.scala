package org.project.scala

object Types {
  opaque type AveragePrice = Double
  opaque type SalesCount = Int
  opaque type RoomCount = Int

  object AveragePrice {
    def apply(value: Double): AveragePrice = value
  }

  object SalesCount {
    def apply(value: Int): SalesCount = value
  }

  object RoomCount {
    def apply(value: Int): RoomCount = value
  }

  extension (x: AveragePrice) {
    def toDouble: Double = x
  }

  extension (x: SalesCount) {
    def toInt: Int = x
  }
}