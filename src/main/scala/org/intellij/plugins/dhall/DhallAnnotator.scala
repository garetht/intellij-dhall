package org.intellij.plugins.dhall

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallBlockComment,
  DhallBuiltin,
  DhallDoubleLiteral,
  DhallDoubleQuoteLiteral,
  DhallExpression,
  DhallIntegerLiteral,
  DhallInterpolation,
  DhallKeyword,
  DhallLetBinding,
  DhallLineComment,
  DhallNaturalLiteral,
  DhallNonEmptyRecordTypeOrLiteral,
  DhallOperator,
  DhallRecordLiteralEntry,
  DhallRecordTypeEntry,
  DhallSelector,
  DhallSingleQuoteLiteral,
  DhallVariable
}

class DhallAnnotator extends Annotator {
  private def textRange(element: PsiElement): TextRange = {
    val textRange = element.getTextRange
    new TextRange(textRange.getStartOffset, textRange.getEndOffset)
  }

  override def annotate(psiElement: PsiElement,
                        annotationHolder: AnnotationHolder): Unit = {
    var highlightannotation =
      annotationHolder.createInfoAnnotation(this.textRange(psiElement), null)

    val color: Option[TextAttributesKey] = psiElement match {
      case _: DhallBlockComment =>
        Some(DefaultLanguageHighlighterColors.BLOCK_COMMENT)
      case _: DhallLineComment =>
        Some(DefaultLanguageHighlighterColors.LINE_COMMENT)
      case _: DhallOperator =>
        Some(DefaultLanguageHighlighterColors.OPERATION_SIGN)
      case _: DhallKeyword => Some(DefaultLanguageHighlighterColors.KEYWORD)
      case _: DhallBuiltin =>
        Some(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
      case _: DhallVariable =>
        Some(DefaultLanguageHighlighterColors.IDENTIFIER)
      case _: DhallSelector =>
        Some(DefaultLanguageHighlighterColors.IDENTIFIER)
      case _: DhallDoubleLiteral =>
        Some(DefaultLanguageHighlighterColors.NUMBER)
      case _: DhallNaturalLiteral =>
        Some(DefaultLanguageHighlighterColors.NUMBER)
      case _: DhallIntegerLiteral =>
        Some(DefaultLanguageHighlighterColors.NUMBER)
      case _: DhallDoubleQuoteLiteral =>
        Some(DefaultLanguageHighlighterColors.STRING)
      case _: DhallInterpolation =>
        Some(DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR)
      case _: DhallSingleQuoteLiteral =>
        Some(DefaultLanguageHighlighterColors.STRING)
      case ne: DhallNonEmptyRecordTypeOrLiteral =>
        // TODO: if the suceeding entry is a non-empty record literal,
        // then this should be colored as a instance value field, otherwise
        // it should be colored as an instance type field
        val label = ne.getLabel
        highlightannotation =
          annotationHolder.createInfoAnnotation(this.textRange(label), null)
        Some(
          if (ne.getNonEmptyRecordLiteral == null)
            DhallSyntaxHighlighter.RECORD_TYPE_KEY
          else DhallSyntaxHighlighter.RECORD_VALUE_KEY
        )
      case rle: DhallRecordLiteralEntry =>
        val label = rle.getLabel
        highlightannotation =
          annotationHolder.createInfoAnnotation(this.textRange(label), null)
        Some(DhallSyntaxHighlighter.RECORD_VALUE_KEY)
      case rle: DhallRecordTypeEntry =>
        val label = rle.getLabel
        highlightannotation =
          annotationHolder.createInfoAnnotation(this.textRange(label), null)
        Some(DhallSyntaxHighlighter.RECORD_TYPE_KEY)
      case lb: DhallLetBinding =>
        val binding = lb.getNonreservedLabel
        highlightannotation =
          annotationHolder.createInfoAnnotation(this.textRange(binding), null)
        Some(DefaultLanguageHighlighterColors.IDENTIFIER)
      case ex: DhallExpression =>
        Option(ex.getParameter)
          .map(param => {
            highlightannotation =
              annotationHolder.createInfoAnnotation(this.textRange(param), null)
            DefaultLanguageHighlighterColors.PARAMETER
          })

      case _ => None
    }

    if (color.isDefined) {
      highlightannotation.setTextAttributes(color.get)
    }
  }
}
// DOT, FUNCTION_DECLARATION (lambda), INSTANCE FIELD (record property)
// what about property accesses?
