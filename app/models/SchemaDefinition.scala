package models

import models.album.Album
import models.artist.Artist
import models.extensions.DateTimeType
import sangria.relay._
import sangria.schema._

import scala.concurrent.ExecutionContext.Implicits.global

object SchemaDefinition {
  val NodeDefinition(nodeInterface, nodeField, nodesField) =
    Node.definition(
      (globalId: GlobalId, ctx: Context[DiscographyRepo, Unit]) => {
        globalId.typeName match {
          case "Album"  => ctx.ctx.getAlbum(globalId.id.toInt)
          case "Artist" => ctx.ctx.getArtist(globalId.id.toInt)
          case _        => None
        }
      },
      Node.possibleNodeTypes[DiscographyRepo, Node](AlbumType, ArtistType)
    )

  def idFields[T: Identifiable] = fields[Unit, T](
    Node.globalIdField,
    Field("rawId", StringType, resolve = ctx => implicitly[Identifiable[T]].id(ctx.value))
  )

  val AlbumType: ObjectType[DiscographyRepo, Album] = ObjectType(
    "Album",
    "An album",
    interfaces[DiscographyRepo, Album](nodeInterface),
    () =>
      fields[DiscographyRepo, Album](
        Node.globalIdField[DiscographyRepo, Album],
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
          Some("Name of the album"),
          resolve = _.value.name
        ),
        Field(
          "artist",
          OptionType(ArtistType),
          Some("The artist"),
          resolve = ctx => ctx.ctx.getArtist(ctx.value.artistId)
        )
      )
  )

  val ConnectionDefinition(_, albumConnection) = Connection
    .definition[DiscographyRepo, Connection, Album]("Album", AlbumType)

  val ArtistType: ObjectType[DiscographyRepo, Artist] = ObjectType(
    "Artist",
    "An artist",
    interfaces[DiscographyRepo, Artist](nodeInterface),
    () =>
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
        ),
        Field(
          "albums",
          albumConnection,
          arguments = Connection.Args.All,
          resolve = ctx => Connection.connectionFromFutureSeq(ctx.ctx.getByArtist(ctx.value.id), ConnectionArgs(ctx))
        )
      )
  )

  val idArgument = Argument("id", IDType)

  val QueryType = ObjectType(
    "Query",
    fields[DiscographyRepo, Unit](
      nodeField,
      nodesField,
      Field(
        "artist",
        OptionType(ArtistType),
        arguments = idArgument :: Nil,
        resolve = ctx => {
          val maybeGlobalId = GlobalId.fromGlobalId(ctx.arg(idArgument))
          maybeGlobalId.map(globalId => ctx.ctx.getArtist(globalId.id.toInt)).get
        }
      ),
      Field(
        "album",
        OptionType(AlbumType),
        arguments = idArgument :: Nil,
        resolve = ctx => {
          val maybeGlobalId = GlobalId.fromGlobalId(ctx.arg(idArgument))
          maybeGlobalId.map(globalId => ctx.ctx.getAlbum(globalId.id.toInt)).get
        }
      )
    )
  )

  val schema = Schema(QueryType, None)
}
