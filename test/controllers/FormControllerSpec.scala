package controllers

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Matchers._
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.http.Status.{NOT_FOUND, OK}
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.test.Helpers.{GET, POST, contentAsString, contentType, defaultAwaitTimeout, route, status, stubControllerComponents, writeableOf_AnyContentAsEmpty}
import play.api.test.{FakeRequest, Injecting}
import play.api.libs.ws.BodyWritable

import scala.concurrent.{ExecutionContext, Future}


class FormControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock = mock[WSClient]
  lazy val wsRequest = mock[WSRequest]
  lazy val wsResponse = mock[WSResponse]

  "FormController GET" should {
    "render the form page from a new instance of controller" in {
      val controller = new FormController(ws, stubControllerComponents(), executionContext)
      val form = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))

      status(form) mustBe OK
      contentType(form) mustBe Some("text/html")
      contentAsString(form) must include ("Vehicle")
    }
    "render the form page from the application" in {
      val controller = inject[FormController]
      val form = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))

      status(form) mustBe OK
      contentType(form) mustBe Some("text/html")
      contentAsString(form) must include ("Vehicle")
    }
    "render the form page from the router" in {
      val request = FakeRequest(GET, "/simpleForm")
      val form = route(app, request).get

      status(form) mustBe OK
      contentType(form) mustBe Some("text/html")
      contentAsString(form) must include ("Vehicle")
    }

    


}
  "FormController POST" should {
    "render the form page from a new instance of controller" in {

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 200
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)

      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.successful(wsResponse)
      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Welcome to Vehicle")
    }
    "fail to render the simpleFormPost page from a new instance of controller" in {

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 404
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)

      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.failed(new Exception)
      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))
      status(result) mustBe NOT_FOUND
    }
  }
}