package org.intellij.plugins.dhall
package editor.selections

import java.util

import com.intellij.codeInsight.editorActions.ExtendWordSelectionHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{DhallCharacter, DhallElementType}

import scala.jdk.CollectionConverters.SeqHasAsJava

class DhallSelectioner extends ExtendWordSelectionHandlerBase {
  override def canSelect(psiElement: PsiElement): Boolean = {
    println("an attempt was made to see if the dhall selectioner could select")
    true
  }

  override def select(psiElement: PsiElement,
                      allEditorBufferText: CharSequence,
                      cursorOffset: Int,
                      editor: Editor): util.List[TextRange] = {
    val originalRange = psiElement.getTextRange
    if (originalRange.getEndOffset > allEditorBufferText.length()) {
      super.select(psiElement, allEditorBufferText, cursorOffset, editor)
    }

    val firstNonCharacterParent = Iterator
      .iterate(psiElement)(_.getParent)
      .drop(1) // take at least one parent
      .dropWhile {
        case _: DhallCharacter => true
        case _                 => false
      }
      .take(1)
      .next()

    List(firstNonCharacterParent.getTextRange).asJava
  }
}
