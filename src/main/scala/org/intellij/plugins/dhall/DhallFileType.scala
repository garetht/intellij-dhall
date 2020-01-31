package org.intellij.plugins.dhall

import org.intellij.plugins.dhall.icons.DhallIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

final class DhallFileType extends LanguageFileType(DhallLanguage) {
  override def getName = "Dhall File"
  override def getDescription = "Dhall Language File"
  override def getDefaultExtension: String = DhallFileType.DhallExtension
  override def getIcon: Icon = DhallIcons.FILE
}

object DhallFileType {
  val DhallExtension = "dhall"
}
