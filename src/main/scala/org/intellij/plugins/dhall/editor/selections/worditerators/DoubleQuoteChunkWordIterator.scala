package org.intellij.plugins.dhall
package editor.selections.worditerators

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallDoubleQuoteChunk

class DoubleQuoteChunkWordIterator(rootElement: DhallDoubleQuoteChunk)
    extends WordIterator(rootElement) {
  // wrapper method that feeds constructor parameters into
  // pre-implemented word boundary methods
  def wordBoundary(): Option[TextRange] = {
    this.boundaryTextRange()
  }

  override def rootElementEligibleForWordSelection(): Boolean = {
    // do not perform word expansion if cursor is at interpolation
    Option(this.rootElement.getInterpolation).isEmpty
  }

  def isWordCharToDrop(element: PsiElement): Boolean = {
    element match {
      case sib: DhallDoubleQuoteChunk => {
        if (Option(sib.getInterpolation).isDefined) {
          false
        } else {
          this.isWordSeparator(sib)
        }
      }
      case _ => false
    }
  }

}

object DoubleQuoteChunkWordIterator {
  def apply(element: DhallDoubleQuoteChunk): DoubleQuoteChunkWordIterator =
    new DoubleQuoteChunkWordIterator(element)

  // The element provided here is any PSI element, and it is the task
  // of the iterator to decide if it should apply, by checking for example
  // to see if the element's grandparent is a double-quote chunk, or a
  // single quote chunk, and so on.
  def generateRange(element: PsiElement): Option[TextRange] = {
    Option(element.getParent)
      .flatMap(par => Option(par.getParent))
      .flatMap {
        case elem: DhallDoubleQuoteChunk =>
          DoubleQuoteChunkWordIterator(elem).wordBoundary()
        case _ => None
      }
  }
}
