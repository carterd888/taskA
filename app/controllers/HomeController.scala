package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def text(inputText: String) = Action { implicit request: Request[AnyContent] =>
      println("input is:" + inputText)
    Ok(views.html.index2(inputText))

  }

  def checkVehicle(vehicle: String)  = Action { implicit request: Request[AnyContent] =>
    println("input is:" + vehicle)

    val carObj = Json.obj(
    "wheels" -> 4,
    "heavy"  -> true,
    "name" -> "BMW"
    )
    val bikeObj = Json.obj(
    "wheels" -> 2,
    "heavy"  -> false,
    "name" -> "Chopper"
    )

    val car = new Vehicle(wheels = 4, heavy = true, name = "BMW")
    val bike = new Vehicle(wheels = 2, heavy = false, name = "Chopper")


    vehicle match{
//      case "car" =>  Ok(Json.prettyPrint(car))
      case "car" => Ok(views.html.index3(car))
      case "bike" => Ok(views.html.index3(bike))
//      case "bike" =>  Ok(Json.prettyPrint(bike)))
      case _ => Ok(views.html.index4("Please specify a vehicle"))
    }
  }


}

class Vehicle(val wheels: Int, val heavy: Boolean, val name: String) {
}
