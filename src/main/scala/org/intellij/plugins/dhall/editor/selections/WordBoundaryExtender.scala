package org.intellij.plugins.dhall
package editor.selections

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

import scala.util.Try

// given a PsiElement, extend the selection both ends
// until we get to word boundaries
trait WordBoundaryExtender {
  // the full boundary
  def wordBoundary(): Option[TextRange]

  // If the examined element is a word character, return
  // true.
  // If it is not a word character, return false. This
  // stops iteration.
  def isWordCharToDrop(element: PsiElement): Boolean
  def entireLiteral(element: PsiElement): TextRange

  def iterateDropWordChars(element: PsiElement,
                           direction: (PsiElement) => PsiElement,
                           offsetEdge: (TextRange) => Int): Option[Int] = {
    Try(
      Iterator
        .iterate(element)(direction)
        .dropWhile(this.isWordCharToDrop)
        .take(1)
        .next()
    ).toOption.map(nw => offsetEdge(nw.getTextRange))
  }

  def boundaryTextRange(element: PsiElement): Option[TextRange] = {
    val wholeLiteralRange = this.entireLiteral(element)
    val previousWordEdge =
      this.iterateDropWordChars(element, _.getPrevSibling, _.getEndOffset)
    val nextWordEdge =
      this.iterateDropWordChars(element, _.getNextSibling, _.getStartOffset)

    Some(
      new TextRange(
        previousWordEdge.getOrElse(wholeLiteralRange.getStartOffset + 1),
        nextWordEdge.getOrElse(wholeLiteralRange.getEndOffset - 1)
      )
    )
  }
}
