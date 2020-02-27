package org.intellij.plugins.dhall
package editor.selections

import java.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallSingleQuoteLiteral

import scala.jdk.CollectionConverters.SeqHasAsJava

class DhallWordSelectioner extends BaseDhallSelectioner {
  override def select(psiElement: PsiElement,
                      allEditorBufferText: CharSequence,
                      cursorOffset: Int,
                      editor: Editor): util.List[TextRange] = {
    val originalRange = psiElement.getTextRange
    if (originalRange.getEndOffset > allEditorBufferText.length()) {
      super.select(psiElement, allEditorBufferText, cursorOffset, editor)
    }

    val range = DoubleQuoteChunkWordIterator
      .generateRange(psiElement)
//      .orElse(
//        SingleQuoteChunkWordIterator
//          .generateRange(psiElement)
//      )

    (if (range.isDefined && range.get.getLength > 0)
       List(range.get)
     else List()).asJava
  }
}
