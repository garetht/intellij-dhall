package org.intellij.plugins.dhall
package annotator

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.{DefaultLanguageHighlighterColors => C}
import org.intellij.plugins.dhall.{DhallSyntaxHighlighter => D}

class SyntaxHighlightAnnotatorTest extends BaseSyntaxHighlightAnnotatorTest {
  def testSimpleHighlighting(): Unit = {
    val highlight = this.highlightFile("simpleLet.dhall")
    this.assertHighlight(
      highlight,
      List(
        AnnotatorHighlightAssertion(text = "let", key = C.KEYWORD),
        AnnotatorHighlightAssertion(text = "x", key = C.IDENTIFIER),
        AnnotatorHighlightAssertion(text = "2", key = C.NUMBER),
        AnnotatorHighlightAssertion(text = "in", key = C.KEYWORD),
        AnnotatorHighlightAssertion(text = "x", key = C.IDENTIFIER)
      )
    )
  }

  def testDoubleQuoteErrorHighlighting(): Unit = {
    val highlight = this.highlightFile("doubleQuoteError.dhall")
    this.assertHighlight(
      highlight,
      List(
        AnnotatorHighlightAssertion(text = "let", key = C.KEYWORD),
        AnnotatorHighlightAssertion(text = "e", key = C.IDENTIFIER),
        AnnotatorHighlightAssertion(text = """"h\ w"""", key = C.STRING),
        AnnotatorHighlightAssertion(
          text = """\""",
          key = C.VALID_STRING_ESCAPE
        ),
        AnnotatorHighlightAssertion(
          text = " ",
          key = null,
          severity = HighlightSeverity.ERROR
        ),
        AnnotatorHighlightAssertion(text = "in", key = C.KEYWORD),
        AnnotatorHighlightAssertion(text = "e", key = C.IDENTIFIER)
      )
    )
  }

  def testUnionTypeHighlighter(): Unit = {
    val highlight = this.highlightFile("unionType.dhall")
    this.assertHighlight(
      highlight,
      List(
        AnnotatorHighlightAssertion(text = "Hello", key = D.UNION_TYPE_ENTRY),
        AnnotatorHighlightAssertion(text = "World", key = D.UNION_TYPE_ENTRY),
        AnnotatorHighlightAssertion(text = "There", key = D.UNION_TYPE_ENTRY),
      )
    )
  }
}
