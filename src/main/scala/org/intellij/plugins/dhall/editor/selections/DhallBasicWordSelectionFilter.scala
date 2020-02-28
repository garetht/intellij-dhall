package org.intellij.plugins.dhall
package editor.selections

import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallElementType

/**
  * The intent of this class is to disable the basic word selection
  * feature that is the default on PSI elements. We disable such selection
  * on all Dhall PSI elements so we can implement our own using the
  * DhallSelectioner.
  *
  * Without this, single characters will automatically get selected.
  */
class DhallBasicWordSelectionFilter extends Condition[PsiElement] {
  override def value(t: PsiElement): Boolean = {
    t.getLanguage != DhallLanguage && (t match {
      case _: DhallElementType => false
      case _                   => true
    })
  }
}
