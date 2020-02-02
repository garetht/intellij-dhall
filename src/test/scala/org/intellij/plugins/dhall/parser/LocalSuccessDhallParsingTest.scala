package org.intellij.plugins.dhall
package parser

class LocalSuccessDhallParsingTest
    extends BaseDhallParsingTest("local/success") {

  def testSimpleLet(): Unit = {
    doTest(true)
  }
}
