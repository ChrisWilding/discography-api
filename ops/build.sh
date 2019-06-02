#!/bin/sh -e
docker build -t cw-discography-api .
echo "$DOCKER_PASSWORD" | docker login --username=_ --password-stdin registry.heroku.com
docker tag cw-discography-api registry.heroku.com/cw-discography-api/web
docker push registry.heroku.com/cw-discography-api/web
