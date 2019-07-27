package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents, RequestHeader}

@Singleton
class PlaygroundController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def playground = Action { implicit request: RequestHeader =>
    val httpOrHttps = if (request.secure) "https" else "http"
    Ok(views.html.playground(httpOrHttps, request.host))
  }
}
