provider "heroku" {
  version = "1.9"
}

resource "heroku_app" "default" {
  name   = "cw-discography-api"
  region = "eu"
  stack  = "container"
}

resource "heroku_addon" "database" {
  app  = "${heroku_app.default.name}"
  plan = "heroku-postgresql:hobby-dev"
}
