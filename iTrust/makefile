default:init start package build run

init:
	brew install maven boot2docker;
	boot2docker init;

start:
	eval "$$(boot2docker shellinit)";
	boot2docker up;

package:
	mvn package -Dmaven.test.skip=true

build:
	docker build -t itrustapp .;

run:
	docker run --net="host" --rm -it -p 8080:8080 -t itrustapp;

ip:
	docker ps -a;

ssh:
	docker run -i -t itrustapp /bin/bash
