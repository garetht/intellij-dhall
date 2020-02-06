package org.intellij.plugins.dhall

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

object DhallSyntaxHighlighter {
  val PATH: TextAttributesKey =
    createTextAttributesKey("PATH", DefaultLanguageHighlighterColors.IDENTIFIER)
  val RECORD_VALUE_KEY: TextAttributesKey = createTextAttributesKey(
    "RECORD_VALUE_KEY",
    DefaultLanguageHighlighterColors.INSTANCE_FIELD
  )
  val RECORD_TYPE_KEY: TextAttributesKey = createTextAttributesKey(
    "RECORD_TYPE_KEY",
    DefaultLanguageHighlighterColors.INSTANCE_FIELD
  )
}

class DhallSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer = {
    new DhallLexerAdapter()
  }

  override def getTokenHighlights(
    iElementType: IElementType
  ): Array[TextAttributesKey] = {
    Array()
  }
}
