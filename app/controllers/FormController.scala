  package controllers

import models.Vehicle
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.libs.ws
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FormController @Inject()(ws: WSClient, cc: ControllerComponents, implicit val ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  def simpleForm() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.form(BasicForm.form))
  }

  def simpleFormPost(): Action[AnyContent] = Action.async { implicit request =>
    val postData = request.body.asFormUrlEncoded

    val vehicleName = postData.map { args =>
      args("Vehicle Name").head
    }.getOrElse(Ok("Error"))

    val dataToBeSent = Json.obj(
      "Vehicle Name" -> s"${vehicleName}"
    )
    val futureResponse: Future[WSResponse] = ws.url("http://localhost:9001/form").post(dataToBeSent)

    futureResponse.map {
      response =>
        val js = Json.fromJson[Vehicle](response.json)
        val veh = js.get
        Ok(views.html.vehicle(veh))
    } recover {
      case _ => NotFound
    }
  }
}
  case class BasicForm(name: String)

  // this could be defined somewhere else,
  // but I prefer to keep it in the companion object
  object BasicForm {
    val form: Form[BasicForm] = Form(
      mapping(
        //      "wheels"    -> number,
        //      "heavy"     -> boolean,
        "name" -> text
      )(BasicForm.apply)(BasicForm.unapply)
    )
  }













