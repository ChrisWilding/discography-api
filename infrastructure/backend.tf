terraform {
  backend "s3" {
    bucket = "terraform.chriswilding.co.uk"
    key    = "discography-api"
    region = "eu-west-1"
  }



}
