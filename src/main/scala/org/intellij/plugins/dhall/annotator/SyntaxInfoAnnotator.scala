package org.intellij.plugins.dhall
package annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.{DhallBashEnvironmentVariable, DhallBlockComment, DhallBuiltin, DhallDotOp, DhallDoubleLiteral, DhallDoubleQuoteChunk, DhallDoubleQuoteLiteral, DhallEnv, DhallEscapedInterpolation, DhallEscapedQuotePair, DhallExpression, DhallHttp, DhallIntegerLiteral, DhallInterpolation, DhallIpLiteral, DhallIpv4Address, DhallKeyword, DhallLabel, DhallLambda, DhallLetBinding, DhallLineComment, DhallLocal, DhallNaturalLiteral, DhallNonEmptyRecordTypeOrLiteral, DhallNonreservedLabel, DhallOperator, DhallPosixEnvironmentVariable, DhallQuotedLabel, DhallRecordLiteralEntry, DhallRecordTypeEntry, DhallSelector, DhallSimpleLabel, DhallSingleQuoteLiteral, DhallUnionTypeEntry, DhallVariable}

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
      case _: DhallIpLiteral | _: DhallIpv4Address =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.IP_LITERAL))
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
      case kw: DhallKeyword =>
        // a label can have a keyword as a prefix
        (defaultTextRange, if (kw.getParent match {
          case _: DhallSimpleLabel => true
          case _ => false
        }) None else Some(DefaultLanguageHighlighterColors.KEYWORD))
      case bi: DhallBuiltin =>
        (
          defaultTextRange,
          if (bi.getParent match {
            case _: DhallNonreservedLabel => true
            case _ => false
          }) None else Some(DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL))
      case dl: DhallLabel =>
        (
          defaultTextRange,
          Option(dl.getParent).flatMap {
            case _: DhallRecordLiteralEntry => Some(DhallSyntaxHighlighter.RECORD_VALUE_KEY)
            case _: DhallRecordTypeEntry => Some(DhallSyntaxHighlighter.RECORD_TYPE_KEY)
            case _ => None
          }
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
      case _: DhallUnionTypeEntry =>
        (defaultTextRange, Some(DhallSyntaxHighlighter.UNION_TYPE_DATA_CONSTRUCTOR))
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
