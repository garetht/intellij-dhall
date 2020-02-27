package org.intellij.plugins.dhall
package editor.selections

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallDoubleQuoteChunk

class DoubleQuoteChunkWordIterator(element: PsiElement)
    extends WordBoundaryExtender {
  // wrapper method that feeds constructor parameters into
  // pre-implemented word boundary methods
  def wordBoundary(): Option[TextRange] = {
    this.boundaryTextRange(element)
  }

  def isWordCharToDrop(element: PsiElement): Boolean = {
    element match {
      case sib: DhallDoubleQuoteChunk => {
        if (Option(sib.getInterpolation).isDefined) {
          false
        } else {
          val t = sib.getText
          t.length > 0 && !t.charAt(0).isWhitespace
        }
      }
      case _ => false
    }
  }

  def entireLiteral(element: PsiElement): TextRange = {
    element.getParent.getTextRange
  }
}

object DoubleQuoteChunkWordIterator {
  def apply(element: PsiElement): DoubleQuoteChunkWordIterator =
    new DoubleQuoteChunkWordIterator(element)

  def generateRange(element: PsiElement): Option[TextRange] = {
    Option(element.getParent)
      .flatMap(par => Option(par.getParent))
      .flatMap {
        case elem: DhallDoubleQuoteChunk =>
          // do not perform word expansion if cursor is at interpolation
          if (Option(elem.getInterpolation).isDefined) None
          else DoubleQuoteChunkWordIterator(elem).wordBoundary()
        case _ => None
      }
  }
}
