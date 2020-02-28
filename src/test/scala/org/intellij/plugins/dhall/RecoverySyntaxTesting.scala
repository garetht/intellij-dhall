package org.intellij.plugins.dhall

trait RecoverySyntaxTesting {
  def testRecordLiteralRecovery(): Unit
  def testLetChainRecovery(): Unit
  def testDoubleQuoteEscapeRecovery(): Unit

  def testForallRecovery(): Unit
  // test forall | if | assert  -> pinning

//  def testDoubleQuoteInterpolationRecovery(): Unit
//  "${hello!}"
}
