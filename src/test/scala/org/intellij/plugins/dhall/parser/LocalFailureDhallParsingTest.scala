package org.intellij.plugins.dhall
package parser

class LocalFailureDhallParsingTest
    extends BaseDhallParsingTest("local/failure") {

  def testincompleteBlockComment(): Unit = {
    doTest(true, false)
  }
}
