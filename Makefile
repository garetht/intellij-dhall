.PHONY: test
test:
	docker build -t intellij-dhall-test .
	docker run intellij-dhall-test
