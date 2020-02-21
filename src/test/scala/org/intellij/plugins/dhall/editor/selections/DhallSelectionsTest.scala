package org.intellij.plugins.dhall
package editor.selections

import org.intellij.plugins.dhall.annotator.HighlightAssert

class DhallSelectionsTest
    extends BaseDhallSelectionsTest
    with GoodSyntaxTesting
    with RecoverySyntaxTesting {
  def testLetExpression(): Unit = {
    this.assertSelectionInText(
      "let attempts = 1 in <selection><caret>1</selection>",
      "<selection>let attempts = 1 in <caret>1</selection>"
    )
  }

  def testAssertExpression(): Unit = {
    this.assertSelectionInText(
      """assert: <selection>2 =<caret>== 2</selection>""",
      "<selection>assert: 2 =<caret>== 2</selection>"
    )
  }

  def testForallExpression(): Unit = {
    this.assertSelectionInText(
      """for<caret>all(x: a) -> b""",
      """<selection>for<caret>all</selection>(x: a) -> b"""
    )

    this.assertSelectionInText(
      """<selection>for<caret>all</selection>(x: a) -> b""",
      """<selection>for<caret>all(x: a) -> b</selection>"""
    )
  }

  def testBuiltinPrefix(): Unit = {
    this.assertSelectionInText(
      "let Na<caret>tural/evensong = 2 in 1",
      "let <selection>Na<caret>tural/evensong</selection> = 2 in 1"
    )
  }

  def testKeywordPrefix(): Unit = {
    this.assertSelectionInText(
      "let asser<caret>tion = 2 in 1",
      "let <selection>asser<caret>tion</selection> = 2 in 1"
    )
  }

  def testUnionType(): Unit = {
    this.assertSelectionInText(
      "<Crimson|Vi<caret>ridian|Azure>",
      "<Crimson|<selection>Vi<caret>ridian</selection>|Azure>"
    )
  }

  def testRecordValueKey(): Unit = {
    this.assertSelectionInText(
      "{ rec<caret>ord_key = 42 }",
      "{ <selection>rec<caret>ord_key</selection> = 42 }"
    )
  }

  def testOperatorPrecedence(): Unit = {
    this.assertSelectionInText(
      "1 === 2 + 3 <selection>!=<caret></selection> 4 && 5",
      "1 === 2 + <selection>3 !=<caret> 4</selection> && 5"
    )
  }

  def testIpV6Literal(): Unit = {
    this.assertSelectionInText(
      "http://[2001<caret>:0db8:85a3:0000:0000:8a2e:0370:7334]",
      "http://[<selection>2001<caret></selection>:0db8:85a3:0000:0000:8a2e:0370:7334]"
    )
  }

  def testIpV4Literal(): Unit = {
    this.assertSelectionInText(
      "http://192.168.0<caret>.1",
      "http://192.168.<selection>0<caret></selection>.1"
    )
  }

  def testLambdaExpression(): Unit = {
    this.assertSelectionInText(
      """\(x: Natural) -> <selection>14 : <caret>forall(y: Y) -> a</selection>""",
      """<selection>\(x: Natural) -> 14 : <caret>forall(y: Y) -> a</selection>"""
    )
  }

  def testIncompleteRecordLiteral(): Unit = {
    this.assertSelectionInText(
      "({<caret>place = 2",
      "({<selection><caret>place</selection> = 2"
    )
  }

  def testSelectorDot(): Unit = {
    this
      .assertSelectionInText(
        "<selection>name<caret></selection>.position",
        "<selection>name<caret>.position</selection>"
      )
  }

  def testIncompleteLetInChain(): Unit = {
    this
      .assertSelectionInText("""
       |let n = 12
       |<selection>l<caret>et</selection> b = {
       |in 1
       |""".stripMargin, """
       |let n = 12
       |<selection>l<caret>et b = {
       |</selection>in 1
       |""".stripMargin)
  }
}
