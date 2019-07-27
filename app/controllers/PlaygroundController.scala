package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents, RequestHeader}

@Singleton
class PlaygroundController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def playground = Action { implicit request: RequestHeader =>
    val httpOrHttps = if (request.host.contains("localhost")) "http" else "https"
    Ok(views.html.playground(httpOrHttps, request.host))
  }
}
