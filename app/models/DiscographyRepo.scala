package models

import javax.inject.Inject
import models.album.{Album, AlbumDAO}
import models.artist.{Artist, ArtistDAO}

import scala.concurrent.Future

class DiscographyRepo @Inject()(artistDAO: ArtistDAO, albumDAO: AlbumDAO) {
  def getAlbum(id: Int): Future[Option[Album]] =
    albumDAO.lookup(id)

  def getArtist(id: Int): Future[Option[Artist]] =
    artistDAO.lookup(id)

  def getByArtist(artistId: Int): Future[Seq[Album]] =
    albumDAO.getByArtist(artistId: Int)
}
