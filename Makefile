.PHONY: test
test:
	docker build -t intellij-dhall-test .
	docker run intellij-dhall-test

.PHONY: clean-parser-gen
clean-parser-gen:
	rm -rf gen/org/intellij/plugins/dhall/parser
	rm -rf gen/org/intellij/plugins/dhall/psi
