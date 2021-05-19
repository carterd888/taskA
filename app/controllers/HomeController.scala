package controllers

import models.Vehicle

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.ws._
import repositories.DataRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(ws:WSClient,
                               val controllerComponents: ControllerComponents,
                               implicit val ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())

  }


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