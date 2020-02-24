package org.intellij.plugins.dhall
package editor.selections

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class BaseDhallSelectionsTest extends BasePlatformTestCase {
  override protected def getTestDataPath =
    "src/test/scala/editorSelectionTestData"

  /**
    * This is the primary test action for selection.
    * @param original The original text with attached caret
    * @param expected The expected selection
    */
  def assertSelectionInText(original: String, expected: String): Unit = {
    this.myFixture.configureByText("dhall.dhall", original)
    this.myFixture
      .performEditorAction(IdeActions.ACTION_EDITOR_SELECT_WORD_AT_CARET)
    this.myFixture.checkResult(expected)
  }
}
