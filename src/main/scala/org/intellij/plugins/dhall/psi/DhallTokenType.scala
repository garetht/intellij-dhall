package org.intellij.plugins.dhall.psi

import com.intellij.psi.tree.IElementType
import org.intellij.plugins.dhall.DhallLanguage

class DhallTokenType(val debugName: String)
    extends IElementType(debugName, DhallLanguage) {
  override def toString = "DhallTokenType." + super.toString
}
