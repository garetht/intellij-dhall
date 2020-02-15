package org.intellij.plugins.dhall
package annotator

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.PsiElement

class DhallAnnotator extends Annotator {

  // Appears to visit the tree in a postorder depth-first traversal
  override def annotate(psiElement: PsiElement,
                        annotationHolder: AnnotationHolder): Unit = {
    val (textRange, color) =
      SyntaxInfoAnnotator.annotate(psiElement)
    if (color.isDefined) {
      val highlightAnnotation =
        annotationHolder.createInfoAnnotation(textRange, null)
      highlightAnnotation.setTextAttributes(color.get)
    }
  }
}
// DOT, FUNCTION_DECLARATION (lambda), INSTANCE FIELD (record property)
// what about property accesses?
