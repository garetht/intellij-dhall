package org.intellij.plugins.dhall
package annotator

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey

case class HighlightAssert(text: String,
                           key: TextAttributesKey,
                           severity: HighlightSeverity =
                             HighlightSeverity.INFORMATION)

object HighlightAssert {
  val AssertError: HighlightAssert =
    HighlightAssert(text = "", key = null, severity = HighlightSeverity.ERROR)

  def fromHighlightInfo(info: HighlightInfo): HighlightAssert = {
    HighlightAssert(
      text = info.getText,
      key = info.forcedTextAttributesKey,
      severity = info.getSeverity
    )
  }
}
