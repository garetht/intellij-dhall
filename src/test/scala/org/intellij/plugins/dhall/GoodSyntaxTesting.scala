package org.intellij.plugins.dhall

trait GoodSyntaxTesting {
  def testLetExpression(): Unit
  def testLambdaExpression(): Unit
  def testForallExpression(): Unit
  def testAssertExpression(): Unit
  def testBuiltinPrefix(): Unit
  def testKeywordPrefix(): Unit
  def testUnionType(): Unit
  def testRecordValueKey(): Unit
  def testOperatorPrecedence(): Unit
  def testIpV6Literal(): Unit
  def testIpV4Literal(): Unit
}
