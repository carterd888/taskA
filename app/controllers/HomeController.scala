package controllers

import models.Vehicle

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.ws._
import repositories.DataRepository

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(ws:WSClient, val controllerComponents: ControllerComponents, dataRepository: DataRepository, implicit val ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())

  }

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def vehicle(vehicleName: String) = Action.async { implicit request: Request[AnyContent] =>
    val futureResult = ws.url(s"http://localhost:9001/checkVehicle/${vehicleName}").get()
    futureResult.map { response =>
      val js = Json.fromJson[Vehicle](response.json)
      val veh = js.get
      Ok(views.html.vehicle(veh))
    } recover {
      case _ => NotFound
    }
  }
}