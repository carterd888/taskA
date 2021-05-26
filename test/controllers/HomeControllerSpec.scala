package controllers
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.ControllerComponents
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{ExecutionContext, Future}


class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock = mock[WSClient]
  lazy val wsRequest = mock[WSRequest]
  lazy val wsResponse = mock[WSResponse]

  "HomeController GET" should {
    "render the index page from a new instance of controller" in {
      val controller = new HomeController(ws, stubControllerComponents(), executionContext)
      val home = controller.index().apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
    ///////////////////////////
    ///////////////////////////
    ///////////////////////////
    "render the vehicle page from the application" in {
      val controller = inject[HomeController]
      val vehicle = controller.vehicle("BMW").apply(FakeRequest(GET, "/"))

      when(wsRequest.get())
      .thenReturn(Future.successful(wsResponse))

      status(vehicle) mustBe OK
      contentType(vehicle) mustBe Some("text/html")
      contentAsString(vehicle) must include ("Welcome to Vehicle")
    }
    "fail to render the vehicle page from the application" in {
      val controller = inject[HomeController]
      val vehicle = controller.vehicle("test").apply(FakeRequest())
      status(vehicle) mustBe NOT_FOUND

    when(wsRequest.get())
      .thenReturn(Future.failed(new Exception))
    }

  }
}