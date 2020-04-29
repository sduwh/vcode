#!/usr/bin/env bash

echo "start pack vcode..."

mvn clean package

echo "Start build docker images..."
echo "Login docker registry"
docker login --username=$1 registry.cn-hangzhou.aliyuncs.com

# shellcheck disable=SC2046
gitTag=$(git describe --tags $(git rev-list --tags --max-count=1))
echo "get the newest tag: ${gitTag}"
dockerTag=$(echo ${gitTag} | egrep '(v[0-9]*\.[0-9]*\.[0-9]*)' -o)

echo "build images..."
docker build --tag registry.cn-hangzhou.aliyuncs.com/vcodeteam/vcode:${dockerTag} -f ./Dockerfile .
docker tag $(docker image ls -q registry.cn-hangzhou.aliyuncs.com/vcodeteam/vcode:${dockerTag}) registry.cn-hangzhou.aliyuncs.com/vcodeteam/vcode:latest

echo "push images..."
docker push registry.cn-hangzhou.aliyuncs.com/vcodeteam/vcode:${dockerTag}
docker push registry.cn-hangzhou.aliyuncs.com/vcodeteam/vcode:latest

echo "pack vcode-vcode docker image success..."
