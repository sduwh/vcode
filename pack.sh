#!/usr/bin/env bash

echo "start pack vcode..."

mvn package

echo "start build docker image"

# shellcheck disable=SC2046
tag=$(git describe --tags $(git rev-list --tags --max-count=1))
echo "get the newest tag: ${tag}"

# build docker
echo "start build docker..."
docker build --tag vcode:"${tag}" -f ./Dockerfile .

echo "pack vcode-vcode docker image success..."
