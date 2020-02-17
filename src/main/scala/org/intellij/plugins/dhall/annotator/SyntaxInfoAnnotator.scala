package org.intellij.plugins.dhall
package annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{
  DhallBashEnvironmentVariable,
  DhallBlockComment,
  DhallBuiltin,
  DhallDotOp,
  DhallDoubleLiteral,
  DhallDoubleQuoteChunk,
  DhallDoubleQuoteLiteral,
  DhallEnv,
  DhallEscapedInterpolation,
  DhallEscapedQuotePair,
  DhallExpression,
  DhallHttp,
  DhallIntegerLiteral,
  DhallInterpolation,
  DhallKeyword,
  DhallLambda,
  DhallLetBinding,
  DhallLineComment,
  DhallLocal,
  DhallNaturalLiteral,
  DhallNonEmptyRecordTypeOrLiteral,
  DhallOperator,
  DhallPosixEnvironmentVariable,
  DhallRecordLiteralEntry,
  DhallRecordTypeEntry,
  DhallSelector,
  DhallSingleQuoteLiteral,
  DhallUnionTypeEntry,
  DhallVariable
}

object SyntaxInfoAnnotator {
  private def textRange(element: PsiElement): TextRange = {
    val textRange = element.getTextRange
    new TextRange(textRange.getStartOffset, textRange.getEndOffset)
  }

  // If there is an error, the default behavior of the Annotator (unless
  // overridden through some highlight extension point) is to skip
  // highlighting of the parent. We override this behavior with the
  // DhallHighlightRangeExtension, which forces all parent nodes to be
  // highlighted in Dhall files.
  def annotate(
    psiElement: PsiElement,
  ): (TextRange, Option[TextAttributesKey]) = {
    val defaultTextRange = this.textRange(psiElement)

    psiElement match {
      case _: DhallBlockComment =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.BLOCK_COMMENT))
      case _: DhallLineComment =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.LINE_COMMENT))

      // Path highlighting
      case _: DhallLocal | _: DhallHttp =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.PATH))

      // Env Highlighting
      case _: DhallEnv =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.ENVIRONMENT_IMPORT))
      case _: DhallBashEnvironmentVariable | _: DhallPosixEnvironmentVariable =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.ENVIRONMENT_IMPORT_NAME))

      // Escape highlighting
      case _: DhallEscapedInterpolation | _: DhallEscapedQuotePair =>
        (
          defaultTextRange,
          Some(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
        )
      case dqc: DhallDoubleQuoteChunk =>
        // the backslash is the only branch of the DoubleQuoteChunk
        // that is the escape
        Option(dqc.getBackslash)
          .map(_ => {
            (
              this.textRange(dqc),
              Some(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
            )
          })
          .getOrElse((defaultTextRange, Option.empty[TextAttributesKey]))
      // N.B. These must come before Operator since they are
      // subclasses of Operator
      case _: DhallLambda =>
        (
          defaultTextRange,
          Some(DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        )
      case _: DhallDotOp =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.DOT))
      case _: DhallOperator =>
        (
          defaultTextRange,
          Some(DefaultLanguageHighlighterColors.OPERATION_SIGN)
        )
      case _: DhallKeyword =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.KEYWORD))
      case _: DhallBuiltin =>
        (
          defaultTextRange,
          Some(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
        )
      case _: DhallVariable | _: DhallSelector =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.IDENTIFIER))
      case _: DhallDoubleLiteral | _: DhallNaturalLiteral |
          _: DhallIntegerLiteral =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.NUMBER))
      case _: DhallDoubleQuoteLiteral | _: DhallSingleQuoteLiteral =>
        (defaultTextRange, Some(DefaultLanguageHighlighterColors.STRING))
      case _: DhallInterpolation =>
        (
          defaultTextRange,
          Some(DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR)
        )
      case ne: DhallNonEmptyRecordTypeOrLiteral =>
        (
          this.textRange(ne.getLabel),
          Some(
            if (ne.getNonEmptyRecordLiteral == null)
              DhallSyntaxHighlighter.RECORD_TYPE_KEY
            else DhallSyntaxHighlighter.RECORD_VALUE_KEY
          )
        )
      case _: DhallUnionTypeEntry =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.UNION_TYPE_ENTRY))
      case rle: DhallRecordLiteralEntry =>
        (
          this.textRange(rle.getLabel),
          Some(DhallSyntaxHighlighter.RECORD_VALUE_KEY)
        )
      case rle: DhallRecordTypeEntry =>
        (
          this.textRange(rle.getLabel),
          Some(DhallSyntaxHighlighter.RECORD_TYPE_KEY)
        )
      case lb: DhallLetBinding =>
        (
          this.textRange(lb.getNonreservedLabel),
          Some(DefaultLanguageHighlighterColors.IDENTIFIER)
        )
      case ex: DhallExpression =>
        // lambda parameters and forall parameters
        Option(ex.getNonreservedLabel)
          .map(label => {
            (
              this.textRange(label),
              Some(DefaultLanguageHighlighterColors.PARAMETER)
            )
          })
          .getOrElse((defaultTextRange, Option.empty[TextAttributesKey]))

      case _ => (defaultTextRange, Option.empty[TextAttributesKey])
    }
  }
}
