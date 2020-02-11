package org.intellij.plugins.dhall
package parser

class LocalSuccessDhallParsingTest
    extends BaseDhallParsingTest("local/success") {

  def testsimpleLet(): Unit = {
    doTest(true, true)
  }

  def testSimpleBlockComment(): Unit = {
    doTest(true, true)
  }
}
