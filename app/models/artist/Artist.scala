package models.artist

import java.time.OffsetDateTime

import sangria.relay.Identifiable

case class Artist(id: Int, createdAt: OffsetDateTime, updatedAt: OffsetDateTime, name: String)

object Artist {
  implicit object ArtistIdentifiable extends Identifiable[Artist] {
    override def id(artist: Artist): String = artist.id.toString
  }
}
