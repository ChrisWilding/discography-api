package models

import models.DiscographyData._

class DiscographyRepo {
  var albums = Albums.All
  var artists = Artists.All

  def getAlbum(id: String): Option[Album] = albums.find(_.id == id)

  def getArtist(id: String): Option[Artist] = artists.find(_.id == id)

  def getArtists(names: Seq[String]): Seq[Option[Artist]] = {
    names.map(getArtistFromName)
  }

  def getArtistFromName(name: String): Option[Artist] = {
    if (name == "Christine and the Queens")
      getChristineAndTheQueens
    else if (name == "The XX")
      getTheXX
    else
      None
  }

  def getChristineAndTheQueens: Option[Artist] = artists.find(_.id == "1")

  def getTheXX: Option[Artist] = artists.find(_.id == "2")
}
