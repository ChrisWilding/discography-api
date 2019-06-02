package controllers

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HelloController @Inject()(cc: ControllerComponents, dbConfigProviders: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {
  val defaultDbConfig = dbConfigProviders.get[JdbcProfile]
  import defaultDbConfig.profile.api._

  def hello(name: String) = Action.async {
    val future = defaultDbConfig.db.run(dbOnePlusOne)

    future.map { values =>
      val value = values.head
      Ok(s"Hello, ${name}! 1 + 1 = ${value}")
    }
  }

  private def dbOnePlusOne =
    sql"""SELECT 1 + 1;""".as[Int]
}
