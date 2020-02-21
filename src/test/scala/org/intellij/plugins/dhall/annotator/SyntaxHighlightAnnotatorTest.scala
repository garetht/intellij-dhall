package org.intellij.plugins.dhall
package annotator

import com.intellij.openapi.editor.{DefaultLanguageHighlighterColors => C}
import org.intellij.plugins.dhall.{DhallSyntaxHighlighter => D}

class SyntaxHighlightAnnotatorTest
    extends BaseSyntaxHighlightAnnotatorTest
    with GoodSyntaxTesting
    with RecoverySyntaxTesting {
  def testLetExpression(): Unit = {
    this.assertHighlight(
      "let x = 2 in x",
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "x", key = C.IDENTIFIER),
        HighlightAssert(text = "2", key = C.NUMBER),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "x", key = C.IDENTIFIER)
      )
    )
  }

  def testDoubleQuoteEscapeError(): Unit = {
    this.assertHighlight(
      """let e = "h\ w" in e""",
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

  def testSelectorDot(): Unit = {
    this.assertHighlight(
      """
        |let n = 2.0
        |let b = a.b
        |in 1
        |""".stripMargin,
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

  def testUnionType(): Unit = {
    this.assertHighlight(
      "<Hello|World|There>",
      List(
        HighlightAssert(text = "Hello", key = D.UNION_TYPE_ENTRY),
        HighlightAssert(text = "World", key = D.UNION_TYPE_ENTRY),
        HighlightAssert(text = "There", key = D.UNION_TYPE_ENTRY),
      )
    )
  }

  def testLambdaExpression(): Unit = {
    this.assertHighlight(
      """\(nat : Natural) -> assert : n === (n + 0)""",
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

  def testIncompleteRecordLiteral(): Unit = {
    this.assertHighlight(
      "({ place = 2",
      List(
        HighlightAssert("place", D.RECORD_VALUE_KEY),
        HighlightAssert("2", C.NUMBER),
        HighlightAssert.assertError("")
      )
    )
  }

  def testKeywordPrefix(): Unit = {
    this.assertHighlight(
      "let assertive = 14 in 1",
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "assertive", key = C.IDENTIFIER),
        HighlightAssert(text = "14", C.NUMBER),
        HighlightAssert(text = "in", C.KEYWORD),
        HighlightAssert(text = "1", C.NUMBER)
      )
    )
  }

  def testBuiltinPrefix(): Unit = {
    this.assertHighlight(
      "let Natural/showoff = 14 in 1",
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "Natural/showoff", key = C.IDENTIFIER),
        HighlightAssert(text = "14", C.NUMBER),
        HighlightAssert(text = "in", C.KEYWORD),
        HighlightAssert(text = "1", C.NUMBER)
      )
    )
  }

  def testIpV4Literal(): Unit = {
    this.assertHighlight(
      """let b = http://192.168.0.0
        |in 1""".stripMargin,
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "b", key = C.IDENTIFIER),
        HighlightAssert(text = "http://192.168.0.0", key = D.PATH),
        HighlightAssert(text = "192.168.0.0", key = D.IP_LITERAL),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "1", key = C.NUMBER),
      )
    )
  }

  def testIpV6Literal(): Unit = {
    this.assertHighlight(
      """let a = http://[2001:0db8:85a3:0000:0000:8a2e:0370:7334]
        |let c = https://[2001:db8::8a2e:370:7334]/hello
        |in 1""".stripMargin,
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
        HighlightAssert(text = "c", key = C.IDENTIFIER),
        HighlightAssert(
          text = "https://[2001:db8::8a2e:370:7334]/hello",
          key = D.PATH
        ),
        HighlightAssert(text = "[2001:db8::8a2e:370:7334]", key = D.IP_LITERAL),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "1", key = C.NUMBER),
      )
    )
  }

  def testForallExpression(): Unit = {
    this.assertHighlight(
      "forall(x: Y) -> 14",
      List(
        HighlightAssert(text = "forall", key = C.KEYWORD),
        HighlightAssert(text = "x", key = C.PARAMETER),
        HighlightAssert(text = "Y", key = C.IDENTIFIER),
        HighlightAssert(text = "->", key = C.OPERATION_SIGN),
        HighlightAssert(text = "14", key = C.NUMBER)
      )
    )
  }

  def testAssertExpression(): Unit = {
    this.assertHighlight(
      "assert: 2 === 2",
      List(
        HighlightAssert(text = "assert", key = C.KEYWORD),
        HighlightAssert(text = "2", key = C.NUMBER),
        HighlightAssert(text = "===", key = C.OPERATION_SIGN),
        HighlightAssert(text = "2", key = C.NUMBER),
      )
    )
  }

  def testRecordValueKey(): Unit = {
    this.assertHighlight(
      "{ record = 14 }",
      List(
        HighlightAssert(text = "record", key = D.RECORD_VALUE_KEY),
        HighlightAssert(text = "14", key = C.NUMBER),
      )
    )
  }

  def testOperatorPrecedence(): Unit = {
    this.assertHighlight(
      """1 + 2 //\\ 3""",
      List(
        HighlightAssert(text = "1", key = C.NUMBER),
        HighlightAssert(text = "+", key = C.OPERATION_SIGN),
        HighlightAssert(text = "2", key = C.NUMBER),
        HighlightAssert(text = """//\\""", key = C.OPERATION_SIGN),
        HighlightAssert(text = "3", key = C.NUMBER),
      )
    )
  }

  def testIncompleteLetInChain(): Unit = {
    this.assertHighlight(
      """
        |let n = 12
        |let b = {
        |in 1
        |""".stripMargin,
      List(
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "n", key = C.IDENTIFIER),
        HighlightAssert(text = "12", key = C.NUMBER),
        HighlightAssert(text = "let", key = C.KEYWORD),
        HighlightAssert(text = "b", key = C.IDENTIFIER),
        HighlightAssert.assertError(text = "i"),
        HighlightAssert(text = "in", key = C.KEYWORD),
        HighlightAssert(text = "1", key = C.NUMBER),
      )
    )
  }
}
