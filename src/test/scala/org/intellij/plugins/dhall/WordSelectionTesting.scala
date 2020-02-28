package org.intellij.plugins.dhall

trait WordSelectionTesting {
  def testDoubleQuoteWordSelectionAdjacentToInterpolation(): Unit
  def testDoubleQuoteWordSelectionExactlyBetweenInterpolation(): Unit

  def testDoubleQuoteWordSelectionAtStringStart(): Unit
  def testDoubleQuoteWordSelectionAtStringEnd(): Unit

  def testDoubleQuoteWordSelectionAtWordStart(): Unit
  def testDoubleQuoteWordSelectionInWordMiddle(): Unit
  def testDoubleQuoteWordSelectionAtWordEnd(): Unit

  // test double quote escape sequence
  // test single quote escape sequences (quote escapes and interpolation escapes)
  def testSingleQuoteWordSelectionAdjacentToInterpolation(): Unit
  def testSingleQuoteWordSelectionExactlyBetweenInterpolation(): Unit
//
  def testSingleQuoteWordSelectionAtStringStart(): Unit
  def testSingleQuoteWordSelectionAtStringEnd(): Unit
//
  def testSingleQuoteWordSelectionAtWordStart(): Unit
  def testSingleQuoteWordSelectionInWordMiddle(): Unit
  def testSingleQuoteWordSelectionAtWordEnd(): Unit
}
