package org.intellij.plugins.dhall
package annotator

import scala.jdk.CollectionConverters.ListHasAsScala
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Assert._

/**
  * The default IntelliJ highlighter tests do not seem to allow asserting text
  * attributes (https://www.jetbrains.org/intellij/sdk/docs/basics/testing_plugins/testing_highlighting.html).
  * In any case, the IntelliJ tools seem to be behave in a manner that requires more
  * understanding of convention than explicitly (when precisely do assertion XML tags get parsed
  * as XML tags and not as part of the source file? why do some XML properties said to be supported
  * in the documentation seem not to be understood?).
  *
  * The utilities here are meant to provide a more explicit and reasonable way of going about these assertions.
  */
abstract class BaseSyntaxHighlightAnnotatorTest extends BasePlatformTestCase {
  override protected def getTestDataPath =
    "src/test/scala/syntaxHighlightTestData"

  def highlightFile(fileName: String): List[HighlightInfo] = {
    this.myFixture.configureByFile(fileName)
    this.myFixture.doHighlighting().asScala.toList
  }

  def assertHighlight(actual: String, expected: List[HighlightAssert]): Unit = {
    this.myFixture.configureByText("highlight.dhall", actual)
    this
      .assertHighlight(this.myFixture.doHighlighting().asScala.toList, expected)
  }

  def assertHighlight(actual: List[HighlightInfo],
                      expected: List[HighlightAssert]): Unit = {
    actual.zip(expected).foreach {
      case (highlightInfo, highlightAssertion) => {
        val selectedInfo =
          HighlightAssert.fromHighlightInfo(highlightInfo)
        val message =
          s"highlight info ${selectedInfo} (actual: ${highlightInfo})\n did not match assertion ${highlightAssertion}"
        // There is the small chance that, because we do not compare the position of the text, our assertion
        // leaves open the possibility of mismatch. This is thought to be an unlikely possibility relative
        // to the amount of noise introducing position information would introduce.
        // (consider let x = 2 let x = 2 in x, and an assertion that asks for the first let to be highlighted
        // but not the second, with no highlights in between, and an actual highlight that reverses this condition)
        assertTrue(message, selectedInfo == highlightAssertion)
      }
    }

    assertEquals(
      s"expected number of highlights to be the same but ${actual.length} highlights were generated and ${expected.length} highlights were asserted.\n\nactual highlights:\n${actual
        .map(HighlightAssert.fromHighlightInfo)
        .map(_.toString)
        .mkString("\n")}",
      actual.length,
      expected.length
    )
  }
}
