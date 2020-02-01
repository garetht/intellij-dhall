package org.intellij.plugins.dhall

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.intellij.plugins.dhall.psi.DhallBlockComment

class DhallAnnotator extends Annotator {
  override def annotate(psiElement: PsiElement,
                        annotationHolder: AnnotationHolder): Unit = {
    val textRange = psiElement.getTextRange
    val offset = new TextRange(textRange.getStartOffset, textRange.getEndOffset)
    val highlightannotation =
      annotationHolder.createInfoAnnotation(offset, null)

    val color: Option[TextAttributesKey] = psiElement match {
      case _: DhallBlockComment =>
        Some(DefaultLanguageHighlighterColors.BLOCK_COMMENT)
      case _ => None
    }

    if (color.isDefined) {
      highlightannotation.setTextAttributes(color.get)
    }
  }
}
