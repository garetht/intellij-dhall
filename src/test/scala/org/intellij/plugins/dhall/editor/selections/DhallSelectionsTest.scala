package org.intellij.plugins.dhall
package editor.selections

class DhallSelectionsTest extends BaseDhallSelectionsTest {
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

  def testOperatorPrecedenceSelection(): Unit = {
    this.assertSelectionInText(
      "1 === 2 + 3 <selection>!=<caret></selection> 4 && 5",
      "1 === 2 + <selection>3 !=<caret> 4</selection> && 5"
    )
  }

  def testIpV6LiteralSelection(): Unit = {
    this.assertSelectionInText(
      "http://[2001<caret>:0db8:85a3:0000:0000:8a2e:0370:7334]",
      "http://[<selection>2001<caret></selection>:0db8:85a3:0000:0000:8a2e:0370:7334]"
    )
  }

  def testIpV4LiteralSelection(): Unit = {
    this.assertSelectionInText(
      "http://192.168.0<caret>.1",
      "http://192.168.<selection>0<caret></selection>.1"
    )
  }
}
