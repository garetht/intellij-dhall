package org.intellij.plugins.dhall
package parser

class LocalSuccessDhallParsingTest
    extends BaseDhallParsingTest("local/success") {

  def testsimpleLet(): Unit = {
    doTest(true, true)
  }

  def testsimpleBlockComment(): Unit = {
    doTest(true, true)
  }

  def testwordedBlockComment(): Unit = {
    doTest(true, true)
  }
}
