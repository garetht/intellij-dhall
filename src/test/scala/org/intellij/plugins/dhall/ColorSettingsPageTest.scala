package org.intellij.plugins.dhall

import com.intellij.openapi.editor.colors.TextAttributesKey
import junit.framework.TestCase
import org.junit.Assert._

import scala.jdk.CollectionConverters.MapHasAsScala

class ColorSettingsPageTest extends TestCase {
  def testAllExampleHighlightsCanBeEdited(): Unit = {
    val colorSettingsPage = new DhallColorSettingsPage()
    val exampleHighlights =
      colorSettingsPage.getAdditionalHighlightingTagToDescriptorMap.asScala.valuesIterator.toSet
    val editableHighlights =
      colorSettingsPage.getAttributeDescriptors.toList.map(_.getKey).toSet

    assertEquals(
      "There were text attributes in the example text but are which are not editable in the settings page.",
      exampleHighlights.diff(editableHighlights),
      Set(),
    )
    assertEquals(
      "There were text attributes editable in the settings page but which are not in the example text",
      editableHighlights.diff(exampleHighlights),
      Set(),
    )
  }

  def testAllCustomDhallSyntaxHighlightsAreEditable(): Unit = {
    val customDhallSyntaxHighlights =
      DhallSyntaxHighlighter.getClass.getDeclaredMethods
        .collect(f => {
          // N.B. If we convert this to an if we lose the
          // type inference that we get from a PartialFunction
          f.getReturnType == classOf[TextAttributesKey] match {
            case true =>
              f.invoke(DhallSyntaxHighlighter).asInstanceOf[TextAttributesKey]
          }
        })
        .toSet
    val colorSettingsPage = new DhallColorSettingsPage()
    val editableHighlights =
      colorSettingsPage.getAttributeDescriptors.toList.map(_.getKey).toSet

    assertEquals(
      "There were custom Dhall syntax highlighting keys that were not editable in the color settings page.",
      customDhallSyntaxHighlights.diff(editableHighlights),
      Set(),
    )
  }
}
