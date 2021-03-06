package models.album

import java.time.OffsetDateTime

import sangria.execution.deferred.HasId
import sangria.relay.Identifiable

case class Album(id: Int, createdAt: OffsetDateTime, updatedAt: OffsetDateTime, artistId: Int, name: String)

object Album {
  implicit object AlbumIdentifiable extends Identifiable[Album] {
    override def id(album: Album): String = album.id.toString
  }

  implicit val hasId = HasId[Album, Int](_.id)
}
