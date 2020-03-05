include docker.Makefile

.PHONY: clean-parser-gen
clean-parser-gen:
	rm -rf gen/org/intellij/plugins/dhall/parser
	rm -rf gen/org/intellij/plugins/dhall/psi
