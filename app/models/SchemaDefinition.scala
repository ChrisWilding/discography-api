package models

import models.artist.Artist
import sangria.relay._
import sangria.schema._
import models.extensions.DateTimeType

object SchemaDefinition {
  val NodeDefinition(nodeInterface, nodeField, nodesField) =
    Node.definition(
      (globalId: GlobalId, ctx: Context[DiscographyRepo, Unit]) => {
        globalId.typeName match {
          case "Artist" => ctx.ctx.getArtist(globalId.id.toInt)
          case _        => None
        }
      },
      Node.possibleNodeTypes[DiscographyRepo, Node](ArtistType)
    )

  def idFields[T: Identifiable] = fields[Unit, T](
    Node.globalIdField,
    Field("rawId", StringType, resolve = ctx => implicitly[Identifiable[T]].id(ctx.value))
  )

  val ArtistType: ObjectType[DiscographyRepo, Artist] = ObjectType(
    "Artist",
    "An artist",
    interfaces[DiscographyRepo, Artist](nodeInterface),
    fields[DiscographyRepo, Artist](
      Node.globalIdField[DiscographyRepo, Artist],
      Field(
        "createAt",
        DateTimeType,
        resolve = _.value.createdAt
      ),
      Field(
        "updatedAt",
        DateTimeType,
        resolve = _.value.updatedAt
      ),
      Field(
        "name",
        StringType,
        Some("Name of the artist"),
        resolve = _.value.name
      )
    )
  )

  val idArgument = Argument("id", IDType)

  val QueryType = ObjectType(
    "Query",
    fields[DiscographyRepo, Unit](
      Field(
        "artist",
        OptionType(ArtistType),
        arguments = idArgument :: Nil,
        resolve = ctx => {
          val maybeGlobalId = GlobalId.fromGlobalId(ctx.arg(idArgument))
          maybeGlobalId.map(globalId => ctx.ctx.getArtist(globalId.id.toInt)).get
        }
      ),
      nodeField,
      nodesField
    )
  )

  val schema = Schema(QueryType, None)
}
