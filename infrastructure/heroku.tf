provider "heroku" {}

resource "heroku_app" "default" {
  name   = "cw-discography-api"
  region = "eu"
  stack  = "container"
}
