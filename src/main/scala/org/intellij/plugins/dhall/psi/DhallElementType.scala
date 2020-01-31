package org.intellij.plugins.dhall.psi

import org.intellij.plugins.dhall.DhallLanguage
import com.intellij.psi.tree.IElementType

class DhallElementType(val debugName: String)
    extends IElementType(debugName, DhallLanguage) {}
