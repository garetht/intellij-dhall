package org.intellij.plugins.dhall

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import javax.swing._

class DhallFile(val viewProvider: FileViewProvider)
    extends PsiFileBase(viewProvider, DhallLanguage) {
  override def getFileType: DhallFileType = new DhallFileType
  override def toString = "Dhall File"
  override def getIcon(flags: Int): Icon = super.getIcon(flags)
}
