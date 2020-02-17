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
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "x", key = C.IDENTIFIER),
        HighlightAssert(text = "2", key = C.NUMBER),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "x", key = C.IDENTIFIER)
      )
    )
  }

  def testDoubleQuoteErrorHighlighting(): Unit = {
    val highlight = this.highlightFile("doubleQuoteError.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "e", key = C.IDENTIFIER),
        HighlightAssert(text = """"h\ w"""", key = C.STRING),
        HighlightAssert(text = """\""", key = C.VALID_STRING_ESCAPE),
        HighlightAssert.assertError(" "),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "e", key = C.IDENTIFIER)
      )
    )
  }

  def testUnionTypeHighlighter(): Unit = {
    val highlight = this.highlightFile("unionType.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert(text = "Hello", key = D.UNION_TYPE_ENTRY),
        HighlightAssert(text = "World", key = D.UNION_TYPE_ENTRY),
        HighlightAssert(text = "There", key = D.UNION_TYPE_ENTRY),
      )
    )
  }

  def testLambdaParameter(): Unit = {
    val highlight = this.highlightFile("lambdaParameter.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert(text = """\""", key = C.FUNCTION_DECLARATION),
        HighlightAssert(text = "nat", key = C.PARAMETER),
        HighlightAssert(text = "Natural", key = C.PREDEFINED_SYMBOL),
        HighlightAssert(text = "->", key = C.OPERATION_SIGN),
        HighlightAssert(text = "assert", key = C.KEYWORD),
        HighlightAssert(text = "n", key = C.IDENTIFIER),
        HighlightAssert(text = "===", key = C.OPERATION_SIGN),
        HighlightAssert(text = "n", key = C.IDENTIFIER),
        HighlightAssert(text = "+", key = C.OPERATION_SIGN),
        HighlightAssert(text = "0", key = C.NUMBER),
      )
    )
  }

  def testIncompleteRecordError(): Unit = {
    val highlight = this.highlightFile("incompleteRecordError.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert("place", D.RECORD_VALUE_KEY),
        HighlightAssert("2", C.NUMBER),
        HighlightAssert.assertError("")
      )
    )
  }

  def testSelectorDot(): Unit = {
    val highlight = this.highlightFile("selectorDot.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "n", key = C.IDENTIFIER),
        HighlightAssert(text = "2.0", key = C.NUMBER),
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "b", key = C.IDENTIFIER),
        HighlightAssert(text = "a", key = C.IDENTIFIER),
        HighlightAssert(text = ".", key = C.DOT),
        HighlightAssert(text = "b", key = C.IDENTIFIER),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "1", key = C.NUMBER),
      )
    )
  }

  def testIpLiteralHighlight(): Unit = {
    val highlight = this.highlightFile("ipLiteral.dhall")
    this.assertHighlight(
      highlight,
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "a", key = C.IDENTIFIER),
        HighlightAssert(
          text = "http://[2001:0db8:85a3:0000:0000:8a2e:0370:7334]",
          key = D.PATH
        ),
        HighlightAssert(
          text = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]",
          key = D.IP_LITERAL
        ),
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "b", key = C.IDENTIFIER),
        HighlightAssert(text = "http://192.168.0.0", key = D.PATH),
        HighlightAssert(text = "192.168.0.0", key = D.IP_LITERAL),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "1", key = C.NUMBER),
      )
    )
  }
}
