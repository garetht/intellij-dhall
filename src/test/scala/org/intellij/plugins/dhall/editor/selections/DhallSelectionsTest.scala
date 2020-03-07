package org.intellij.plugins.dhall
package editor.selections

import org.intellij.plugins.dhall.annotator.HighlightAssert

class DhallSelectionsTest
    extends BaseDhallSelectionsTest
    with CorrectSyntaxTesting
    with RecoverySyntaxTesting
    with WordSelectionTesting {
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

  def testRecordLiteralRecovery(): Unit = {
    this.assertSelectionInText(
      "({<caret>place = 2",
      "({<selection><caret>place</selection> = 2"
    )
  }

  override def testRecordNewFieldRecovery(): Unit = {
    this.assertSelectionInText(
      "{place = 2, <caret>world =}",
      "{place = 2, <selection><caret>world</selection> =}"
    )
  }

  override def testPropertyAccessRecovery(): Unit = {
    this.assertSelectionInText(
      "first.se<caret>cond.third.",
      "first.<selection>se<caret>cond</selection>.third."
    )
  }

  def testSelectorDot(): Unit = {
    this
      .assertSelectionInText(
        "<selection>name<caret></selection>.position",
        "<selection>name<caret>.position</selection>"
      )
  }

  def testLetChainRecovery(): Unit = {
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

  def testDoubleQuoteEscapeRecovery(): Unit = {
    this.assertSelectionInText(
      """let e = <caret>"h\ w" in e""",
      """let e = <selection><caret>"h\ w"</selection> in e"""
    )
  }

  def testForallRecovery(): Unit = {
    this.assertSelectionInText(
      """\(x: T) -> f : <selection>forall<caret></selection>(x: X) let x = 2""",
      """\(x: T) -> f : <selection>forall<caret>(x: X) let x = 2</selection>"""
    )
  }

  def testDoubleQuoteInterpolationRecovery(): Unit = {
    this
      .assertSelectionInText(
        """<caret>"${hello!}"""",
        """<selection><caret>"${hello!}"</selection>"""
      )
  }

  def testInterpolation(): Unit = {
    this.assertSelectionInText(
      """"text <caret>${variable}"""",
      """"text <selection><caret>${variable}</selection>""""
    )
  }

  def testWithinInterpolation(): Unit = {
    this.assertSelectionInText(
      """"text ${<caret>variable}"""",
      """"text ${<selection><caret>variable</selection>}""""
    )
  }

  def testDoubleQuoteWordSelectionAdjacentToInterpolation(): Unit = {
    this.assertSelectionInText(
      """"riverr<caret>un${variable}"""",
      """"<selection>riverr<caret>un</selection>${variable}""""
    )
  }

  def testDoubleQuoteWordSelectionExactlyBetweenInterpolation(): Unit = {
    this.assertSelectionInText(
      """"riverrun<caret>${variable}"""",
      """"riverrun<selection>${variable}</selection>""""
    )
  }

  // test word selection adjacent to escape
  // test word selection adjacent to block comment

  def testDoubleQuoteWordSelectionAtWordStart(): Unit = {
    this.assertSelectionInText(
      """"pet <caret>the dog"""",
      """"pet <selection><caret>the</selection> dog""""
    )
  }

  def testDoubleQuoteWordSelectionInWordMiddle(): Unit = {
    this.assertSelectionInText(
      """"pet th<caret>e dog"""",
      """"pet <selection>th<caret>e</selection> dog""""
    )
  }

  def testDoubleQuoteWordSelectionAtWordEnd(): Unit = {
    this.assertSelectionInText(
      """"pet the<caret> dog"""",
      """"pet <selection>the<caret></selection> dog""""
    )
  }

  def testDoubleQuoteWordSelectionAtStringStart(): Unit = {
    this.assertSelectionInText(
      """"<caret>pet the dog"""",
      """"<caret><selection>pet</selection> the dog""""
    )
  }

  def testDoubleQuoteWordSelectionAtStringEnd(): Unit = {
    this.assertSelectionInText(
      """"pet the dog<caret>"""",
      """"pet the <selection>dog<caret></selection>""""
    )
  }

  def testSingleQuoteWordSelectionAtWordStart(): Unit = {
    this.assertSelectionInText("""''
     | word-sel <caret>inco ntent
     | string''
     |""".stripMargin, """''
     | word-sel <selection><caret>inco</selection> ntent
     | string''
     |""".stripMargin)
  }

  def testSingleQuoteWordSelectionAtWordEnd(): Unit = {
    this.assertSelectionInText("""''
     | word-sel inco ntent<caret>
     | string''
     |""".stripMargin, """''
     | word-sel inco <selection>ntent<caret></selection>
     | string''
     |""".stripMargin)
  }

  def testSelectionAtBlockCommentStart(): Unit = {
    this.assertSelectionInText(
      "{-<caret>text content-}1",
      "{-<selection><caret>text</selection> content-}1"
    )
  }

  def testSelectionAtBlockCommentEnd(): Unit = {
    this.assertSelectionInText(
      "{-text content<caret>-}1",
      "{-text <selection>content<caret></selection>-}1"
    )
  }

  def testSelectionOfBlockCommentStartSyntax(): Unit = {
    this.assertSelectionInText(
      "{<caret>-text content-}1",
      "<selection>{<caret>-text content-}</selection>1"
    )
  }

  def testSelectionOfBlockCommentEndSyntax(): Unit = {
    this.assertSelectionInText(
      "{-text content-<caret>}1",
      "<selection>{-text content-<caret>}</selection>1"
    )
  }

  def testSelectionOfNestedBlockComment(): Unit = {
    this.assertSelectionInText(
      "{-text {-<selection> n<caret>ested </selection>-} content-}1",
      "{-text <selection>{- n<caret>ested -}</selection> content-}1"
    )
  }

  def testSelectionAtLineCommentStart(): Unit = {
    this.assertSelectionInText(
      """
        | let x = 2 --<caret>assign variable
        | in x""".stripMargin,
      """
        | let x = 2 --<selection><caret>assign</selection> variable
        | in x""".stripMargin
    )
  }

  def testSelectionOfLineCommentSyntax(): Unit = {
    this.assertSelectionInText("""
        | let x = 2 -<caret>-assign variable
        | in x""".stripMargin, """
        | let x = 2 <selection>-<caret>-assign variable
        |</selection> in x""".stripMargin)
  }

  def testSingleQuoteWordSelectionInWordMiddle(): Unit = {
    this.assertSelectionInText("""''
        | word-<caret>sel in
        | string''
        |""".stripMargin, """''
        | <selection>word-<caret>sel</selection> in
        | string''
        |""".stripMargin)
  }

  def testSingleQuoteWordSelectionAtStringStart(): Unit = {
    this.assertSelectionInText("""''<caret>
       | word-sel in
       | string''
       |""".stripMargin, """<selection>''<caret>
       | word-sel in
       | string''</selection>
       |""".stripMargin)
  }

  def testSingleQuoteWordSelectionAtStringEnd(): Unit = {
    this.assertSelectionInText("""''
       | word-sel in
       | string<caret>''
       |""".stripMargin, """''
       | word-sel in
       | <selection>string<caret></selection>''
       |""".stripMargin)
  }

  def testSingleQuoteWordSelectionAdjacentToInterpolation(): Unit = {
    this.assertSelectionInText(
      """''
       | word interpol<caret>ated${variable}
       | string''
       |""".stripMargin,
      """''
       | word <selection>interpol<caret>ated</selection>${variable}
       | string''
       |""".stripMargin
    )
  }

  def testSingleQuoteWordSelectionExactlyBetweenInterpolation(): Unit = {
    this.assertSelectionInText(
      """''
        | word interpolated<caret>${variable}
        | string''
        |""".stripMargin,
      """''
        | word interpolated<selection><caret>${variable}</selection>
        | string''
        |""".stripMargin
    )
  }

  def testSingleQuoteEscapedInterpolation(): Unit = {
    this.assertSelectionInText("""''
      | con ''${var<caret>iable}es''
      |""".stripMargin, """''
      | con <selection>''${var<caret>iable}es</selection>''
      |""".stripMargin)
    this.assertSelectionInText("""''
      | con ''<caret>${variable}es''
      |""".stripMargin, """''
      | con <selection>''<caret>${</selection>variable}es''
      |""".stripMargin)
  }

  def testSingleQuoteEscapedTwoSingleQuotes(): Unit = {
    this.assertSelectionInText("""''
       | pers<caret>on'''s''
       |""".stripMargin, """''
       | <selection>pers<caret>on'''s</selection>''
       |""".stripMargin)
  }

  def testDoubleQuoteEscapeSequence(): Unit = {
    this.assertSelectionInText(
      """"this """ + """\""" + """u<caret>{2931}"""",
      """"this """ + """<selection>\""" + """u<caret>{2931}</selection>""""
    )
  }

  def testBlockComment(): Unit = {
    this.assertSelectionInText(
      """
         |{- hello
         | wor<caret>ld -}1""".stripMargin,
      """
         |{- hello
         | <selection>wor<caret>ld</selection> -}1""".stripMargin
    )
  }

  def testNestedBlockComment(): Unit = {
    this.assertSelectionInText(
      """
        |{- hello
        | wor{- inn<caret>er world -} ld -}1""".stripMargin,
      """
        |{- hello
        | wor{- <selection>inn<caret>er</selection> world -} ld -}1""".stripMargin
    )
  }

  def testSingleLineComment(): Unit = {
    this.assertSelectionInText(
      """let x = 2 --<caret>assign variable
        |in x
        |""".stripMargin,
      """let x = 2 --<selection><caret>assign</selection> variable
        |in x
        |""".stripMargin
    )
  }
}
