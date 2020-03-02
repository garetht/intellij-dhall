package org.intellij.plugins.dhall
package editor.selections.worditerators

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallCharacter,
  DhallEscapedInterpolation,
  DhallEscapedQuotePair,
  DhallInterpolation,
  DhallSingleQuoteLiteral
}

class SingleQuoteChunkWordIterator(rootElement: PsiElement)
    extends WordIterator(rootElement) {
  // wrapper method that feeds constructor parameters into
  // pre-implemented word boundary methods
  def wordBoundary(): Option[TextRange] = {
    this.boundaryTextRange()
  }

  override def rootElementEligibleForWordSelection(): Boolean = {
    // do not perform word expansion if cursor is at interpolation
    this.rootElement match {
      case _: DhallInterpolation => false
      case _                     => true
    }
  }

  def isWordCharToDrop(element: PsiElement): Boolean = {
    element match {
      case c: DhallCharacter                                       => this.isWordSeparator(c)
      case _: DhallEscapedInterpolation | _: DhallEscapedQuotePair => true
      case _                                                       => false
    }
  }
}

object SingleQuoteChunkWordIterator {
  def apply(element: PsiElement): SingleQuoteChunkWordIterator =
    new SingleQuoteChunkWordIterator(element)

  def generateRange(element: PsiElement): Option[TextRange] = {
    Option(element.getParent).flatMap(par => {
      Option(par.getParent).flatMap {
        case _: DhallSingleQuoteLiteral =>
          SingleQuoteChunkWordIterator(par).wordBoundary()
        case _ => None
      }
    })
  }
}
