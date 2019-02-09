package models

import sangria.relay.{Identifiable, Node}

object DiscographyData {
  case class Album(id: String, name: String) extends Node

  case class Artist(id: String, name: String, albums: List[String])

  object Artist {
    implicit object ArtistIdentifiable extends Identifiable[Artist] {
      def id(artist: Artist): String = artist.id
    }
  }

  object Albums {
    val chaleurHumaine = Album("1", "Chaleur Hamaine")
    val chris = Album("2", "Chris")

    val xx = Album("3", "XX")
    val coexist = Album("4", "Coexist")
    val iSeeYou = Album("5", "I See You")

    val All = chaleurHumaine :: chris :: xx :: coexist :: iSeeYou :: Nil
  }

  object Artists {
    val christineAndTheQueens = Artist("1", "Christine and the Queens", List("1", "2"))
    val theXX = Artist("2", "The XX", List("3", "4", "5"))

    val All = christineAndTheQueens :: theXX :: Nil
  }
}
