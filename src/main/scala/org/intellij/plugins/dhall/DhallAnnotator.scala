package org.intellij.plugins.dhall

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallBashEnvironmentVariable,
  DhallBlockComment,
  DhallBuiltin,
  DhallDoubleLiteral,
  DhallDoubleQuoteLiteral,
  DhallEnv,
  DhallExpression,
  DhallHttp,
  DhallIntegerLiteral,
  DhallInterpolation,
  DhallKeyword,
  DhallLetBinding,
  DhallLineComment,
  DhallLocal,
  DhallNaturalLiteral,
  DhallNonEmptyRecordTypeOrLiteral,
  DhallOperator,
  DhallPath,
  DhallPosixEnvironmentVariable,
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

      // Path highlighting
      case _: DhallLocal | _: DhallHttp =>
        Some(DhallSyntaxHighlighter.PATH)

      // Env Highlighting
      case _: DhallEnv =>
        Some(DhallSyntaxHighlighter.ENVIRONMENT_IMPORT)
      case _: DhallBashEnvironmentVariable | _: DhallPosixEnvironmentVariable =>
        Some(DhallSyntaxHighlighter.ENVIRONMENT_IMPORT_NAME)

      case _: DhallOperator =>
        Some(DefaultLanguageHighlighterColors.OPERATION_SIGN)
      case _: DhallKeyword => Some(DefaultLanguageHighlighterColors.KEYWORD)
      case _: DhallBuiltin =>
        Some(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
      case _: DhallVariable | _: DhallSelector =>
        Some(DefaultLanguageHighlighterColors.IDENTIFIER)
      case _: DhallDoubleLiteral | _: DhallNaturalLiteral |
          _: DhallIntegerLiteral =>
        Some(DefaultLanguageHighlighterColors.NUMBER)
      case _: DhallDoubleQuoteLiteral | _: DhallSingleQuoteLiteral =>
        Some(DefaultLanguageHighlighterColors.STRING)
      case _: DhallInterpolation =>
        Some(DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR)
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
