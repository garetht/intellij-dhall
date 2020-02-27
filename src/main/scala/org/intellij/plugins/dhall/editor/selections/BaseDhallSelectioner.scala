package org.intellij.plugins.dhall
package editor.selections

import com.intellij.codeInsight.editorActions.ExtendWordSelectionHandlerBase
import com.intellij.psi.PsiElement

class BaseDhallSelectioner extends ExtendWordSelectionHandlerBase {
  override def canSelect(psiElement: PsiElement): Boolean = {
    psiElement.getLanguage == DhallLanguage
  }
}
