package org.intellij.plugins.dhall

import com.intellij.lexer.FlexAdapter
import java.io.Reader

class DhallLexerAdapter()
    extends FlexAdapter(new _DhallLexer(null.asInstanceOf[Reader])) {}
