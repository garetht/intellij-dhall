package org.intellij.plugins.dhall
package extensions

import com.intellij.codeInsight.daemon.impl.HighlightRangeExtension
import com.intellij.psi.PsiFile

class DhallHighlightRangeExtension extends HighlightRangeExtension {
  override def isForceHighlightParents(psiFile: PsiFile): Boolean = {
    psiFile.getFileType match {
      case _: DhallFileType => true
      case _                => false
    }
  }
}
