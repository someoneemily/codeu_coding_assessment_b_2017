// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;
import java.util.*;
import static java.lang.System.*;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {
  private String source;
  private int index;
  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source.trim();
    index = 0;
  }


  @Override
  public Token next() throws IOException {

      // Most of your work will take place here. For every call to |next| you should
      // return a token until you reach the end. When there are no more tokens, you
      // should return |null| to signal the end of input.
      // If for any reason you detect an error in the input, you may throw an IOException
      // which will stop all execution.

      // no more tokens
      // return null to signal end of input
      if(index == source.length()) {
          return null;
      }

      // At end of string
      if(source.charAt(index)==';') {
          index++;
          return new SymbolToken(';');
      }

      boolean inQuote = false;
      String str = "";

      while(index<source.length()){
          //beginning of quote
          if((""+source.charAt(index)).equals("\"") && !inQuote){
              inQuote = true;
              index++;
              continue;
          }
          //end of quote
          if((""+source.charAt(index)).equals("\"") && inQuote){
              index++;
              return new StringToken(str.trim());
          }
          if(!inQuote && source.charAt(index)==' ' && str.trim().length()>=1){
              index++;
              break;
          }
          if(source.charAt(index)==';'){
              break;
          }
          // no space between operators and numbers
          if(str.trim().length()>0 && isSymbol(source.charAt(index))){
              break;
          }else if(isSymbol(source.charAt(index))){
              // symbol by itself
              index++;
              return new SymbolToken(source.charAt(index-1));
          }
          str+=source.charAt(index);
          index++;

      }

      str = str.trim();

      // checks to make sure the digit is a double
      try {
          return new NumberToken(Double.parseDouble(str));
      }catch(NumberFormatException e){
          if(str.length()>=1)
              return new NameToken(str);
          throw new IOException("Error with input statement");
      }

  }

  public boolean isSymbol(char c){
      return c == '=' || c == '+' || c == '-';
  }
}
