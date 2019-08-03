package models.artist

import java.sql.Timestamp
import java.time.{Instant, OffsetDateTime, ZoneId}

import discography.slick.Tables
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ArtistDAO @Inject()(dbConfigProviders: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  val defaultDbConfig = dbConfigProviders.get[JdbcProfile]
  import defaultDbConfig.profile.api._

  def lookup(id: Int): Future[Option[Artist]] = {
    val future = defaultDbConfig.db.run(Tables.Artist.filter(_.id === id).result.headOption)
    future.map(maybeRow => maybeRow.map(artistRowToArtist))
  }

  private def artistRowToArtist(artistRow: Tables.ArtistRow): Artist = {
    Artist(artistRow.id, artistRow.createdAt, artistRow.updatedAt, artistRow.name)
  }

  implicit def timestampToOffsetDateTime(timestamp: Timestamp): OffsetDateTime = {
    val milliSinceEpoch = timestamp.getTime();
    val instant = Instant.ofEpochMilli(milliSinceEpoch)
    OffsetDateTime.ofInstant(instant, ZoneId.of("UTC"))
  }
}
