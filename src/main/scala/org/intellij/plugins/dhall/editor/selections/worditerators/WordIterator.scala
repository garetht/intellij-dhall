package org.intellij.plugins.dhall
package editor.selections.worditerators

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

import scala.util.Try

// given a PsiElement, extend the selection both ends
// until we get to word boundaries
abstract class WordIterator[T <: PsiElement](rootElement: T) {
  def rootElementEligibleForWordSelection(): Boolean = true

  // generates the full text range that bounds the word. If
  // None, then this WordIterator relinquishes the opportunity
  // to offer a word selection. This may happen, for example,
  // when the cursor is at an interpolation.
  def wordBoundary(): Option[TextRange]

  def isWordSeparator(e: PsiElement): Boolean = {
    val t = e.getText
    t.length > 0 && !t.charAt(0).isWhitespace
  }

  /**
    * If the examined element is a word character, return
    * true.
    * If it is not a word character, return false. This
    * stops iteration.
    * @param element The element to examine for word-characterness.
    * @return
    */
  def isWordCharToDrop(element: PsiElement): Boolean

  /**
    * @return The containing literal relevant to the root element
    *         being examined.
    */
  def entireLiteral(): TextRange = {
    this.rootElement.getParent.getTextRange
  }

  /**
    * Iterates in the direction of the param direction, dropping
    * characters while they are word characters until it finds the
    * a non-word character. Depending on which direction we are iterating
    * in, choose an offset for the non-word character. For example, if we
    * are iterating backwards, then the word boundary is at the end of the
    * non-word character (_ | a b c), but if we are iterating forwards it is
    * at the start (a b c | _)
    * @param element The element to be checked for in direction
    * @param direction The direction in which to iterate.
    * @param offsetEdge Whether to get the start or end offset of the non-dropped chararcter.
    * @return
    */
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

  def boundaryTextRange(): Option[TextRange] = {
    val wholeLiteralRange = this.entireLiteral()

    if (!this.rootElementEligibleForWordSelection) {
      return None
    }

    val previousWordEdge =
      this.iterateDropWordChars(
        this.rootElement,
        _.getPrevSibling,
        _.getEndOffset
      )
    val nextWordEdge =
      this.iterateDropWordChars(
        this.rootElement,
        _.getNextSibling,
        _.getStartOffset
      )

    Some(
      new TextRange(
        previousWordEdge.getOrElse(wholeLiteralRange.getStartOffset + 1),
        nextWordEdge.getOrElse(wholeLiteralRange.getEndOffset - 1)
      )
    )
  }
}
