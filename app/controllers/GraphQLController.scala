package controllers

import javax.inject._
import models.{DiscographyRepo, SchemaDefinition}
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.renderer.SchemaRenderer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class GraphQLController @Inject()(cc: ControllerComponents, discographyRepo: DiscographyRepo)
    extends AbstractController(cc) {

  def graphql(query: String, variables: Option[String], operation: Option[String]) = Action.async {
    executeQuery(query, variables.map(parseVariables), operation)
  }

  def graphqlBody = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(parseVariables(vars))
      case obj: JsObject  => Some(obj)
      case _              => None
    }

    executeQuery(query, variables, operation)
  }

  def schema = Action {
    Ok(SchemaRenderer.renderSchema(SchemaDefinition.schema))
  }

  private def executeQuery(
      query: String,
      variables: Option[JsObject],
      operation: Option[String]
  ) = {
    QueryParser.parse(query) match {
      case Success(queryAst) =>
        Executor
          .execute(
            SchemaDefinition.schema,
            queryAst,
            discographyRepo,
            operationName = operation,
            variables = variables getOrElse Json.obj()
          )
          .map(Ok(_))
          .recover {
            case error: QueryAnalysisError => BadRequest(error.resolveError)
            case error: ErrorWithResolver  => InternalServerError(error.resolveError)
          }

      case Failure(error: SyntaxError) =>
        Future.successful(
          BadRequest(
            Json.obj(
              "syntaxError" -> error.getMessage,
              "locations" -> Json.arr(
                Json.obj(
                  "line" -> error.originalError.position.line,
                  "column" -> error.originalError.position.column
                )
              )
            )
          )
        )

      case Failure(error) =>
        throw error
    }
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj()
    else Json.parse(variables).as[JsObject]
}
