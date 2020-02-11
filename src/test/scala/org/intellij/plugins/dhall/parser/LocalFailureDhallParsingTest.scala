package org.intellij.plugins.dhall
package parser

class LocalFailureDhallParsingTest
    extends BaseDhallParsingTest("local/failure") {

  def testIncompleteBlockComment(): Unit = {
    doTest(true, false)
  }
}
