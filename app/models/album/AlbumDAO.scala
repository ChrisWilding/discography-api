package models.album

import java.sql.Timestamp
import java.time.{Instant, OffsetDateTime, ZoneId}

import discography.slick.Tables
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class AlbumDAO @Inject()(dbConfigProviders: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  val defaultDbConfig = dbConfigProviders.get[JdbcProfile]
  import defaultDbConfig.profile.api._

  def lookup(id: Int): Future[Option[Album]] = {
    val future = defaultDbConfig.db.run(Tables.Album.filter(_.id === id).result.headOption)
    future.map(maybeRow => maybeRow.map(albumRowToAlbum))
  }

  def getByArtist(artistId: Int): Future[Seq[Album]] = {
    val future = defaultDbConfig.db.run(Tables.Album.filter(_.artistId === artistId).result)
    future.map(_.map(albumRowToAlbum))
  }

  private def albumRowToAlbum(albumRow: Tables.AlbumRow): Album = {
    Album(albumRow.id, albumRow.createdAt, albumRow.updatedAt, albumRow.artistId, albumRow.name)
  }

  implicit def timestampToOffsetDateTime(timestamp: Timestamp): OffsetDateTime = {
    val milliSinceEpoch = timestamp.getTime;
    val instant = Instant.ofEpochMilli(milliSinceEpoch)
    OffsetDateTime.ofInstant(instant, ZoneId.of("UTC"))
  }
}
