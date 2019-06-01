package models

import models.DiscographyData._
import sangria.relay._
import sangria.schema._

object SchemaDefinition {
  val NodeDefinition(nodeInterface, nodeField, nodesField) =
    Node.definition(
      (globalId: GlobalId, ctx: Context[DiscographyRepo, Unit]) => {
        if (globalId.typeName == "Album")
          ctx.ctx.getAlbum(globalId.id)
        else if (globalId.typeName == "Artist")
          ctx.ctx.getArtist(globalId.id)
        else
          None
      },
      Node.possibleNodeTypes[DiscographyRepo, Node](AlbumType)
    )

  def idFields[T: Identifiable] = fields[Unit, T](
    Node.globalIdField,
    Field("rawId", StringType, resolve = ctx => implicitly[Identifiable[T]].id(ctx.value))
  )

  val AlbumType: ObjectType[Unit, Album] = ObjectType(
    "Album",
    "An album",
    interfaces[Unit, Album](nodeInterface),
    idFields[Album] ++ fields[Unit, Album](
      Field("name", OptionType(StringType), Some("The name of the album."), resolve = _.value.name)
    )
  )

  val ConnectionDefinition(_, albumConnection) = Connection
    .definition[DiscographyRepo, Connection, Option[Album]]("Album", OptionType(AlbumType))

  val ArtistType: ObjectType[DiscographyRepo, Artist] = ObjectType(
    "Artist",
    "An artist",
    interfaces[DiscographyRepo, Artist](nodeInterface),
    fields[DiscographyRepo, Artist](
      Node.globalIdField[DiscographyRepo, Artist],
      Field(
        "name",
        OptionType(StringType),
        Some("The name of the artist."),
        resolve = _.value.name
      ),
      Field(
        "albums",
        OptionType(albumConnection),
        arguments = Connection.Args.All,
        resolve = ctx =>
          Connection.connectionFromSeq(ctx.value.albums map ctx.ctx.getAlbum, ConnectionArgs(ctx))
      )
    )
  )

  val namesArgument = Argument("names", ListInputType(StringType))

  val QueryType = ObjectType(
    "Query",
    fields[DiscographyRepo, Unit](
      Field(
        "artists",
        ListType(OptionType(ArtistType)),
        arguments = namesArgument :: Nil,
        resolve = ctx => ctx.ctx.getArtists(ctx.arg(namesArgument))
      ),
      Field(
        "christineAndTheQueens",
        OptionType(ArtistType),
        resolve = _.ctx.getChristineAndTheQueens
      ),
      Field("theXX", OptionType(ArtistType), resolve = _.ctx.getTheXX),
      nodeField,
      nodesField
    )
  )

  val schema = Schema(QueryType, None)
}
