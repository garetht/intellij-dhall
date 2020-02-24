package org.intellij.plugins.dhall
package psi

import com.intellij.psi.tree.IElementType

class DhallElementType(val debugName: String)
    extends IElementType(debugName, DhallLanguage) {}
