package org.intellij.plugins.dhall
package editor.selections.worditerators

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallBlockComment,
  DhallCharacter,
  DhallLineComment
}

class LineCommentWordIterator(rootElement: PsiElement)
    extends WordIterator(rootElement) {
  // wrapper method that feeds constructor parameters into
  // pre-implemented word boundary methods
  def wordBoundary(): Option[TextRange] = {
    this.boundaryTextRange()
  }

  def isWordCharToDrop(element: PsiElement): Boolean = {
    element match {
      case c: DhallCharacter => this.isWordSeparator(c)
      case _                 => false
    }
  }
}

object LineCommentWordIterator {
  def apply(element: PsiElement): LineCommentWordIterator =
    new LineCommentWordIterator(element)

  // The element provided here is any PSI element, and it is the task
  // of the iterator to decide if it should apply, by checking for example
  // to see if the element's grandparent is a double-quote chunk, or a
  // single quote chunk, and so on.
  def generateRange(element: PsiElement): Option[TextRange] = {
    Option(element.getParent).flatMap(par => {
      Option(par.getParent).flatMap {
        case _: DhallLineComment =>
          LineCommentWordIterator(par).wordBoundary()
        case _ => None
      }
    })
  }
}
