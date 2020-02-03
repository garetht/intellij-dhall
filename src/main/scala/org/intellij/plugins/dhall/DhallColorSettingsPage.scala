package org.intellij.plugins.dhall

import java.util

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.{
  AttributesDescriptor,
  ColorDescriptor,
  ColorSettingsPage
}
import javax.swing._
import org.intellij.plugins.dhall.icons.DhallIcons

import scala.jdk.CollectionConverters.MapHasAsJava

object DhallColorSettingsPage {
  private val DESCRIPTORS = Array[AttributesDescriptor](
    new AttributesDescriptor("Path", DhallSyntaxHighlighter.PATH),
    new AttributesDescriptor("Record Key", DhallSyntaxHighlighter.RECORD_KEY),
  )
}

class DhallColorSettingsPage extends ColorSettingsPage {
  override def getIcon: Icon = DhallIcons.FILE
  override def getDemoText: String =
    """
      | <block-comment>{- A block comment -}</block-comment>
      |
      | <builtin>Natural/fold</builtin> <number>10</number> <builtin>Text</builtin> (<operator>λ</operator>(<parameter>t</parameter> : <builtin>Text</builtin>) → <identifier>t</identifier> ++ <string>"!"</string>) <string>"Hello"</string>
      |
      | <line-comment>-- A line comment</line-comment>
      |
      | <keyword>let</keyword> <identifier>x</identifier> = <number>1</number> <operator>+</operator> <number>1.0</number> <operator>//\\</operator> <number>Infinity</number> <operator>||</operator> <number>NaN</number>
      |
      | let Example = { Type = { foo : Natural, bar : Bool }, default = { bar = False } }
      | in  Example::{ foo = 1 }
      |""".stripMargin
  override def getAdditionalHighlightingTagToDescriptorMap
    : util.Map[String, TextAttributesKey] =
    Map(
      "block-comment" -> DefaultLanguageHighlighterColors.BLOCK_COMMENT,
      "line-comment" -> DefaultLanguageHighlighterColors.LINE_COMMENT,
      "identifier" -> DefaultLanguageHighlighterColors.IDENTIFIER,
      "keyword" -> DefaultLanguageHighlighterColors.KEYWORD,
      "builtin" -> DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL,
      "operator" -> DefaultLanguageHighlighterColors.OPERATION_SIGN,
      "number" -> DefaultLanguageHighlighterColors.NUMBER,
      "string" -> DefaultLanguageHighlighterColors.STRING,
      "parameter" -> DefaultLanguageHighlighterColors.PARAMETER
    ).asJava
  override def getAttributeDescriptors: Array[AttributesDescriptor] =
    DhallColorSettingsPage.DESCRIPTORS
  override def getColorDescriptors: Array[ColorDescriptor] =
    ColorDescriptor.EMPTY_ARRAY
  override def getDisplayName = "Dhall"

  override def getHighlighter: SyntaxHighlighter = {
    new DhallSyntaxHighlighter()
  }
}
