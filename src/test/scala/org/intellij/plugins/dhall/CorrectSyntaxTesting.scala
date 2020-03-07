package org.intellij.plugins.dhall

trait CorrectSyntaxTesting {
  def testLetExpression(): Unit
  def testLambdaExpression(): Unit
  def testForallExpression(): Unit
  def testAssertExpression(): Unit
  def testBuiltinPrefix(): Unit
  def testKeywordPrefix(): Unit
  def testUnionType(): Unit
  def testRecordValueKey(): Unit
//  def testRecordTypeKey(): Unit
  def testOperatorPrecedence(): Unit
  def testSelectorDot(): Unit
  def testIpV6Literal(): Unit
  def testIpV4Literal(): Unit
  def testInterpolation(): Unit
  def testWithinInterpolation(): Unit

  def testSingleQuoteEscapedInterpolation(): Unit
  def testSingleQuoteEscapedTwoSingleQuotes(): Unit

  def testDoubleQuoteEscapeSequence(): Unit

  def testBlockComment(): Unit
  def testNestedBlockComment(): Unit
  def testSingleLineComment(): Unit
  // testImport
  // testAssertApplicationExpression
  // testMergeApplicationExpression
}
