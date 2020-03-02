package org.intellij.plugins.dhall

trait WordSelectionTesting {
  def testDoubleQuoteWordSelectionAdjacentToInterpolation(): Unit
  def testDoubleQuoteWordSelectionExactlyBetweenInterpolation(): Unit

  def testDoubleQuoteWordSelectionAtStringStart(): Unit
  def testDoubleQuoteWordSelectionAtStringEnd(): Unit

  def testDoubleQuoteWordSelectionAtWordStart(): Unit
  def testDoubleQuoteWordSelectionInWordMiddle(): Unit
  def testDoubleQuoteWordSelectionAtWordEnd(): Unit

  def testSingleQuoteWordSelectionAdjacentToInterpolation(): Unit
  def testSingleQuoteWordSelectionExactlyBetweenInterpolation(): Unit

  def testSingleQuoteWordSelectionAtStringStart(): Unit
  def testSingleQuoteWordSelectionAtStringEnd(): Unit

  def testSingleQuoteWordSelectionAtWordStart(): Unit
  def testSingleQuoteWordSelectionInWordMiddle(): Unit
  def testSingleQuoteWordSelectionAtWordEnd(): Unit

  def testSelectionAtBlockCommentStart(): Unit
  def testSelectionAtBlockCommentEnd(): Unit

  def testSelectionOfBlockCommentStartSyntax(): Unit
  def testSelectionOfBlockCommentEndSyntax(): Unit
  def testSelectionOfNestedBlockComment(): Unit
}
