package models

import javax.inject.Inject
import models.artist.{Artist, ArtistDAO}

import scala.concurrent.Future

class DiscographyRepo @Inject()(artistDAO: ArtistDAO) {
  def getArtist(id: Int): Future[Option[Artist]] =
    artistDAO.lookup(id)
}
