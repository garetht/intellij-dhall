package org.intellij.plugins.dhall
package parser

class LocalFailureDhallParsingTest
    extends BaseDhallParsingTest("local/failure") {

  def testincompleteBlockComment(): Unit = {
    doTest(true, false)
  }

  def testincompleteDoubleQuoteSlash(): Unit = {
    doTest(true, false)
  }

  def testletBindingRecovery(): Unit = {
    doTest(true, false)
  }

  def testfirstApplicationExpressionRecovery(): Unit = {
    doTest(true, false)
  }
}
