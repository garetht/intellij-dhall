package org.intellij.plugins.dhall
package editor.selections

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallCharacter,
  DhallDoubleQuoteChunk,
  DhallEscapedInterpolation,
  DhallEscapedQuotePair,
  DhallSingleQuoteLiteral
}

class SingleQuoteChunkWordIterator(element: PsiElement)
    extends WordBoundaryExtender {
  // wrapper method that feeds constructor parameters into
  // pre-implemented word boundary methods
  def wordBoundary(): Option[TextRange] = {
    this.boundaryTextRange(element)
  }

  def isWordCharToDrop(element: PsiElement): Boolean = {
    element match {
      case c: DhallCharacter => {
        val t = c.getText
        t.length > 0 && !t.charAt(0).isWhitespace
      }
      case _: DhallEscapedInterpolation | _: DhallEscapedQuotePair => true
      case _                                                       => false
    }
  }

  def entireLiteral(element: PsiElement): TextRange = {
    element.getParent.getTextRange
  }
}

object SingleQuoteChunkWordIterator {
  def apply(element: PsiElement): SingleQuoteChunkWordIterator =
    new SingleQuoteChunkWordIterator(element)

  def generateRange(element: PsiElement): Option[TextRange] = {
    Option(element.getParent).flatMap(par => {
      Option(par.getParent).flatMap {
        case _: DhallSingleQuoteLiteral =>
          SingleQuoteChunkWordIterator.generateRange(par)
        case _ => None
      }
    })
  }
}
