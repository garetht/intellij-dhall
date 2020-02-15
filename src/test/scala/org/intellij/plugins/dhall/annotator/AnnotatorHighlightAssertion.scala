package org.intellij.plugins.dhall
package annotator

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey

case class AnnotatorHighlightAssertion(text: String,
                                       key: TextAttributesKey,
                                       severity: HighlightSeverity =
                                         HighlightSeverity.INFORMATION)

object AnnotatorHighlightAssertion {
  def fromHighlightInfo(info: HighlightInfo): AnnotatorHighlightAssertion = {
    AnnotatorHighlightAssertion(
      text = info.getText,
      key = info.forcedTextAttributesKey,
      severity = info.getSeverity
    )
  }
}
