package org.intellij.plugins.dhall;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.intellij.plugins.dhall.psi.DhallTypes.*;

%%

%{
  public _DhallLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _DhallLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

TAB=\x09
SPACE=\x20
DOUBLE_QUOTE=\x22
UALPHA=[GHJPQRUVWXY]
BACKSLASH=\x5C
GRAVE=\x60
LALPHA=[jqz]
DELETE=\x7F
UNICODE_FORALL=\u2200
UNICODE_COMBINE=\u2227
UNICODE_COMBINE_TYPES=\u2A53
UNICODE_EQUIVALENT=\u2261
UNICODE_PREFER=\u2AFD
UNICODE_LAMBDA=\u03BB
UNICODE_ARROW=\u2192
NEW_LINE=\x0a
CARRIAGE_RETURN=\x0d
VALID_NON_ASCII=[\u0080-\uD7FF\uE000-\uFFFD\U010000-\U01FFFD\U020000-\U02FFFD\U030000-\U03FFFD\U040000-\U04FFFD\U050000-\U05FFFD\U060000-\U06FFFD\U070000-\U07FFFD\U080000-\U08FFFD\U090000-\U09FFFD\U0A0000-\U0AFFFD\U0B0000-\U0BFFFD\U0C0000-\U0CFFFD\U0D0000-\U0DFFFD\U0E0000-\U0EFFFD\U0F0000-\U0FFFFD\U100000-\U10FFFD]

%%
<YYINITIAL> {
  "!"                          { return EXCLAMATION; }
  "#"                          { return HASH_SIGN; }
  "$"                          { return DOLLAR; }
  "%"                          { return PERCENT; }
  "&"                          { return AMPERSAND; }
  "'"                          { return SINGLE_QUOTE; }
  "("                          { return LEFT_PAREN; }
  ")"                          { return RIGHT_PAREN; }
  "*"                          { return ASTERISK; }
  "+"                          { return PLUS; }
  ","                          { return COMMA; }
  "-"                          { return MINUS; }
  "."                          { return DOT; }
  "/"                          { return SLASH; }
  "0"                          { return ZERO; }
  "1"                          { return ONE; }
  "2"                          { return TWO; }
  "3"                          { return THREE; }
  "4"                          { return FOUR; }
  "5"                          { return FIVE; }
  "6"                          { return SIX; }
  "7"                          { return SEVEN; }
  "8"                          { return EIGHT; }
  "9"                          { return NINE; }
  ":"                          { return COLON; }
  ";"                          { return SEMICOLON; }
  "<"                          { return LT; }
  "="                          { return EQUALS; }
  ">"                          { return GT; }
  "?"                          { return QUESTION; }
  "@"                          { return AT; }
  "A"                          { return UA; }
  "B"                          { return UB; }
  "C"                          { return UC; }
  "D"                          { return UD; }
  "E"                          { return UE; }
  "F"                          { return UF; }
  "I"                          { return UI; }
  "K"                          { return UK; }
  "L"                          { return UL; }
  "M"                          { return UM; }
  "N"                          { return UN; }
  "O"                          { return UO; }
  "S"                          { return US; }
  "T"                          { return UT; }
  "Z"                          { return UZ; }
  "["                          { return LEFT_SQUARE_BRACKET; }
  "]"                          { return RIGHT_SQUARE_BRACKET; }
  "^"                          { return CIRCUMFLEX; }
  "_"                          { return UNDERSCORE; }
  "a"                          { return A; }
  "b"                          { return B; }
  "c"                          { return C; }
  "d"                          { return D; }
  "e"                          { return E; }
  "f"                          { return F; }
  "g"                          { return G; }
  "h"                          { return H; }
  "i"                          { return I; }
  "k"                          { return K; }
  "l"                          { return L; }
  "m"                          { return M; }
  "n"                          { return N; }
  "o"                          { return O; }
  "p"                          { return P; }
  "r"                          { return R; }
  "s"                          { return S; }
  "t"                          { return T; }
  "u"                          { return U; }
  "v"                          { return V; }
  "w"                          { return W; }
  "x"                          { return X; }
  "y"                          { return Y; }
  "{"                          { return LEFT_BRACE; }
  "|"                          { return PIPE; }
  "}"                          { return RIGHT_BRACE; }
  "~"                          { return TILDE; }

  {TAB}                        { return TAB; }
  {SPACE}                      { return SPACE; }
  {DOUBLE_QUOTE}               { return DOUBLE_QUOTE; }
  {UALPHA}                     { return UALPHA; }
  {BACKSLASH}                  { return BACKSLASH; }
  {GRAVE}                      { return GRAVE; }
  {LALPHA}                     { return LALPHA; }
  {DELETE}                     { return DELETE; }
  {UNICODE_FORALL}             { return UNICODE_FORALL; }
  {UNICODE_COMBINE}            { return UNICODE_COMBINE; }
  {UNICODE_COMBINE_TYPES}      { return UNICODE_COMBINE_TYPES; }
  {UNICODE_EQUIVALENT}         { return UNICODE_EQUIVALENT; }
  {UNICODE_PREFER}             { return UNICODE_PREFER; }
  {UNICODE_LAMBDA}             { return UNICODE_LAMBDA; }
  {UNICODE_ARROW}              { return UNICODE_ARROW; }
  {NEW_LINE}                   { return NEW_LINE; }
  {CARRIAGE_RETURN}            { return CARRIAGE_RETURN; }
  {VALID_NON_ASCII}            { return VALID_NON_ASCII; }

}

[^] { return BAD_CHARACTER; }
