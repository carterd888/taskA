package repositories

import models.Vehicle

import javax.inject.Singleton

@Singleton
class DataRepository {
  private val vehicle = Seq(
    Vehicle(4, true, "BMW"),
    Vehicle(2, false, "Chopper")
  )
  def getVehicle(vehicleNameFromUrl: String): Option[Vehicle] = vehicle.collectFirst {
    case p if p.name == vehicleNameFromUrl => p
    case _ => null
  }
}