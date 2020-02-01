package org.intellij.plugins.dhall

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.intellij.plugins.dhall.parser.DhallParser
import org.intellij.plugins.dhall.psi.DhallTypes

object DhallParserDefinition {
  val FILE = new IFileElementType(DhallLanguage)
}
class DhallParserDefinition extends ParserDefinition {
  override def createLexer(project: Project) = new DhallLexerAdapter
  override def createParser(project: Project) = new DhallParser
  override def getFileNodeType: IFileElementType = DhallParserDefinition.FILE
  override def getCommentTokens: TokenSet = TokenSet.EMPTY
  override def getStringLiteralElements: TokenSet = TokenSet.EMPTY
  override def createElement(astNode: ASTNode): PsiElement =
    DhallTypes.Factory.createElement(astNode)
  override def createFile(fileViewProvider: FileViewProvider) =
    new DhallFile(fileViewProvider)
}
