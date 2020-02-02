package org.intellij.plugins.dhall
package parser

import java.io.File

import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.ParsingTestCase

// ParsingTestCase finds the full path to the test data
// by combining the getTestDataPath with the dataPath in the
// ParsingTestCase constructor, in that order, to determine
// where the test data directory is.
// In this case, the test data directory is src/test/scala/fixtures/<dataPath>
//
// The test file consisting of <filename>.<fileext> (in the ParsingTestCase
// constructor) and <filename>.txt (the PSI file) is determined by
// a method named test<filename>: Unit in the test class itself.
abstract class BaseDhallParsingTest(dataPath: String)
    extends ParsingTestCase(dataPath, "dhall", new DhallParserDefinition) {

  override def getTestDataPath: String =
    "src/test/scala/parseTestData"

  // Regrettably, the default load file implementation
  // trims whitespace away, thus modifying the precise
  // contents of the file we specify.
  // (https://intellij-support.jetbrains.com/hc/en-us/community/posts/360001800000-ParsingTestCase-ignores-trailing-whitespace)
  // This implementation preserves the file precisely
  // as it was loaded.
  override def loadFile(name: String) = {
    FileUtil.loadFile(new File(this.myFullDataPath, name), "UTF-8", true)
  }

  override def skipSpaces(): Boolean = {
    false
  }

  override def includeRanges(): Boolean = {
    true
  }
}
