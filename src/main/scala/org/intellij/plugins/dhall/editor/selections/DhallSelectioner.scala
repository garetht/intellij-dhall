package org.intellij.plugins.dhall
package editor.selections

import java.util

import com.intellij.codeInsight.editorActions.ExtendWordSelectionHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallBuiltin,
  DhallCharacter,
  DhallDigit,
  DhallDoubleQuoteEscaped,
  DhallHexdig,
  DhallKeyword,
  DhallNonEmptyRecordLiteral,
  DhallNonEmptyRecordType,
  DhallNonEmptyRecordTypeOrLiteral,
  DhallNonreservedLabel,
  DhallNotEndOfLine,
  DhallPathComponent,
  DhallQuotedPathComponent,
  DhallRecordTypeOrLiteral,
  DhallSimpleLabel,
  DhallUnicodeEscape,
  DhallUnquotedPathComponent
}

import scala.jdk.CollectionConverters.SeqHasAsJava

class DhallSelectioner extends ExtendWordSelectionHandlerBase {
  override def canSelect(psiElement: PsiElement): Boolean = {
    psiElement.getLanguage == DhallLanguage
  }

  override def select(psiElement: PsiElement,
                      allEditorBufferText: CharSequence,
                      cursorOffset: Int,
                      editor: Editor): util.List[TextRange] = {
    val originalRange = psiElement.getTextRange
    if (originalRange.getEndOffset > allEditorBufferText.length()) {
      super.select(psiElement, allEditorBufferText, cursorOffset, editor)
    }

    val firstNonCharacterParent = Iterator
      .iterate(psiElement)(_.getParent)
      .drop(1) // take at least one parent
      .dropWhile {
        case _: DhallCharacter             => true
        case _: DhallDigit                 => true
        case _: DhallHexdig                => true
        case _: DhallNonEmptyRecordLiteral => true
        case _: DhallNonEmptyRecordType    => true
        // DhallRecordTypeOrLiteral will not select the
        // braces surrounding the record type so we ignore
        // it in favor of PrimitiveExpr
        case _: DhallNonEmptyRecordTypeOrLiteral => true
        case _: DhallRecordTypeOrLiteral         => true
        // Double Quote Escaping
        case _: DhallUnicodeEscape      => true
        case _: DhallDoubleQuoteEscaped => true

        // Path Component Escaping
        case _: DhallPathComponent         => true
        case _: DhallQuotedPathComponent   => true
        case _: DhallUnquotedPathComponent => true

        case _: DhallNotEndOfLine => true

        // Handle keywords that are prefixes of labels
        // and builtins that are prefixes of reserved labels
        case kw: DhallKeyword =>
          kw.getParent match {
            case _: DhallSimpleLabel => true
            case _                   => false
          }
        case bi: DhallBuiltin =>
          bi.getParent match {
            case _: DhallNonreservedLabel => true
            case _                        => false
          }
        case _ => false
      }
      .take(1)
      .next()

    List(firstNonCharacterParent.getTextRange).asJava
  }
}