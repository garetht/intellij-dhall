package org.intellij.plugins.dhall
package parser

class CanonicalSuccessTextDhallParsingTest
    extends BaseDhallParsingTest("canonical/success/text") {

  def testdollarSignA(): Unit = {
    doTest(true, true)
  }

  def testdoubleQuotedStringA(): Unit = {
    doTest(true, true)
  }

  def testescapeA(): Unit = {
    doTest(true, true)
  }
}
