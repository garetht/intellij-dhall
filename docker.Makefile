CONTAINER_NAME = intellij-dhall-container

.PHONY: docker-build
docker-build:
	docker build -t $(CONTAINER_NAME) .

.PHONY: docker-test
docker-test: docker-build
	docker run $(CONTAINER_NAME) test

.PHONY: docker-artifact
docker-artifact: docker-build
	docker run $(CONTAINER_NAME) packageArtifact
