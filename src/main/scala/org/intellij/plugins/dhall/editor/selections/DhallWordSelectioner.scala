package org.intellij.plugins.dhall
package editor.selections

import java.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallDoubleQuoteChunk

import scala.jdk.CollectionConverters.SeqHasAsJava
import scala.util.Try

class DhallWordSelectioner extends BaseDhallSelectioner {

  override def select(psiElement: PsiElement,
                      allEditorBufferText: CharSequence,
                      cursorOffset: Int,
                      editor: Editor): util.List[TextRange] = {
    val originalRange = psiElement.getTextRange
    if (originalRange.getEndOffset > allEditorBufferText.length()) {
      super.select(psiElement, allEditorBufferText, cursorOffset, editor)
    }

    val range: Option[TextRange] = Option(psiElement.getParent)
      .flatMap(par => Option(par.getParent))
      .flatMap {
        case elem: DhallDoubleQuoteChunk =>
          // do not perform word expansion if cursor is at interpolation
          if (Option(elem.getInterpolation).isDefined) None
          else Some(elem)
        case _ => None
      }
      .flatMap(element => {
        // check for comment structure
        // and check for double quote structure
        // and check for single quote structure
        // get start and end of string in case previous word or
        // next word are empty

        val wholeLiteralRange = element.getParent.getTextRange
        val previousWord =
          DhallWordSelectioner.iterateInDirectionUntilRangeEdge(
            element,
            _.getPrevSibling,
            _.getEndOffset
          )
        val nextWord = DhallWordSelectioner.iterateInDirectionUntilRangeEdge(
          element,
          _.getNextSibling,
          _.getStartOffset
        )

        Some(
          new TextRange(
            previousWord.getOrElse(wholeLiteralRange.getStartOffset + 1),
            nextWord.getOrElse(wholeLiteralRange.getEndOffset - 1)
          )
        )
      })

    (if (range.isDefined && range.get.getLength > 0) List(range.get)
     else List()).asJava
  }
}

object DhallWordSelectioner {
  // Iterates the element's related elements until
  // whitespace is reached. Also, we can choose whether
  // we want the start or end offset of a word depending
  // on which direction we are iterating towards
  private def iterateInDirectionUntilRangeEdge(
    element: PsiElement,
    direction: (PsiElement) => PsiElement,
    offsetEdge: (TextRange) => Int
  ): Option[Int] = {
    Try(
      Iterator
        .iterate(element)(direction)
        .dropWhile {
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
        .take(1)
        .next()
    ).toOption.map(nw => offsetEdge(nw.getTextRange))
  }
}
