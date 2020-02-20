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
}
