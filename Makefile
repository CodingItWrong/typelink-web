SHELL := /bin/bash

LOCAL_BUILD_NAME=typelink

pre-build:
	# build the image
	docker build -t $(LOCAL_BUILD_NAME)-build -f BuildDockerfile .

	# run the container so built jar file can be retrieved
	docker run -d --name $(LOCAL_BUILD_NAME)-build $(LOCAL_BUILD_NAME)-build

	# retrieve built jar file out of container
	docker cp $(LOCAL_BUILD_NAME)-build:/myapp/target/ROOT.war .

	# stop running container
	docker rm /$(LOCAL_BUILD_NAME)-build
