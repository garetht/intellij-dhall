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
    new AttributesDescriptor(
      "Environment Import",
      DhallSyntaxHighlighter.ENVIRONMENT_IMPORT
    ),
    new AttributesDescriptor(
      "Environment Variable Import Name",
      DhallSyntaxHighlighter.ENVIRONMENT_IMPORT_NAME
    ),
    new AttributesDescriptor(
      "Record Value Key",
      DhallSyntaxHighlighter.RECORD_VALUE_KEY
    ),
    new AttributesDescriptor(
      "Record Type Key",
      DhallSyntaxHighlighter.RECORD_TYPE_KEY
    ),
    new AttributesDescriptor(
      "Block Comment",
      DefaultLanguageHighlighterColors.BLOCK_COMMENT
    ),
    new AttributesDescriptor(
      "Line Comment",
      DefaultLanguageHighlighterColors.LINE_COMMENT
    ),
    new AttributesDescriptor("Number", DefaultLanguageHighlighterColors.NUMBER),
    new AttributesDescriptor("String", DefaultLanguageHighlighterColors.STRING),
    new AttributesDescriptor(
      "Operator",
      DefaultLanguageHighlighterColors.OPERATION_SIGN
    ),
    new AttributesDescriptor(
      "Interpolation",
      DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
    ),
  )
}

class DhallColorSettingsPage extends ColorSettingsPage {
  override def getIcon: Icon = DhallIcons.FILE
  override def getDemoText: String =
    s"""
      |<block-comment>{- A block comment -}</block-comment>
      |
      |<keyword>let</keyword> <identifier>f</identifier> = <path>./src/data/directory/file.dhall</path>
      |
      |<keyword>let</keyword> <identifier>hname</identifier> = <env-import>env:<env-import-name>HOME_DIRECTORY</env-import-name></env-import>
      |
      |<keyword>let</keyword> <identifier>f</identifier> = <builtin>Natural/fold</builtin> <number>10</number> <builtin>Text</builtin> (<operator>λ</operator>(<parameter>textParam</parameter> : <builtin>Text</builtin>) <operator>→</operator> <identifier>t</identifier> <operator>++</operator> <string>"!"</string>) <string>"Hello <interpolation>$${</interpolation><string>"world"</string><interpolation>}</interpolation>"</string>
      |
      |<keyword>let</keyword> <identifier>x</identifier> = <number>1</number> <operator>+</operator> <number>1.0</number> <operator>⩓</operator> <number>Infinity</number> <operator>||</operator> <number>NaN</number>
      |
      |<keyword>let</keyword> <identifier>Example</identifier> =
      |      { <record-value-key>Type</record-value-key> = { <record-type-key>foo</record-type-key> : <builtin>Natural</builtin>, <record-type-key>bar</record-type-key> : <builtin>Bool</builtin> }, <record-value-key>default</record-value-key> = { <record-value-key>bar</record-value-key> = <builtin>False</builtin> } }
      |
      |<keyword>in</keyword>  <identifier>Example</identifier><operator>::</operator>{ <record-value-key>value</record-value-key> = <number>1</number> }
      |
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
      "parameter" -> DefaultLanguageHighlighterColors.PARAMETER,
      "record-type-key" -> DhallSyntaxHighlighter.RECORD_TYPE_KEY,
      "record-value-key" -> DhallSyntaxHighlighter.RECORD_VALUE_KEY,
      "interpolation" -> DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR,
      "path" -> DhallSyntaxHighlighter.PATH,
      "env-import" -> DhallSyntaxHighlighter.ENVIRONMENT_IMPORT,
      "env-import-name" -> DhallSyntaxHighlighter.ENVIRONMENT_IMPORT_NAME,
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
