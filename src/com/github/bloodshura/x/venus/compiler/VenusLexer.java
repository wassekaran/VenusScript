/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.compiler;

import com.github.bloodshura.x.charset.TextBuilder;
import com.github.bloodshura.x.charset.sequence.CharSet;
import com.github.bloodshura.x.collection.store.impl.XQueue;
import com.github.bloodshura.x.venus.compiler.Token.Type;
import com.github.bloodshura.x.venus.exception.compile.UnexpectedInputException;
import com.github.bloodshura.x.venus.origin.ScriptOrigin;
import com.github.bloodshura.x.worker.StringWorker;

import java.io.IOException;

import static com.github.bloodshura.x.venus.compiler.VenusLexer.State.*;

public class VenusLexer {
	private final TextBuilder buildingToken;
	private boolean insideComment;
	private char lastChar;
	private int line;
	private Type numberLiteralType;
	private final ScriptOrigin origin;
	private int position;
	private final XQueue<Token> reread;
	private State state;
	private final String string;

	public VenusLexer(ScriptOrigin origin) throws IOException {
		this.buildingToken = new TextBuilder();
		this.origin = origin;
		this.reread = new XQueue<>();
		this.string = origin.read();
	}

	public int currentLine() {
		return line;
	}

	public Token nextToken() throws UnexpectedInputException {
		if (!reread.isEmpty()) {
			return reread.poll();
		}

		while (canRead()) {
			char ch = read();

			try {
				boolean isDigit = Character.isDigit(ch);
				boolean isLetter = Character.isLetter(ch) || ch == '_';

				if (ch == '\n') {
					this.insideComment = false;

					if (state == IN_CHAR_LITERAL) {
						bye("End of line found, but unclosed character literal");
					}

					if (state == IN_STRING_LITERAL) {
						bye("End of line found, but unclosed string literal");
					}

					if (state == IN_NAME_DEFINITION) {
						back();

						this.state = null;

						return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
					}

					if (state == IN_NUMBER_LITERAL) {
						back();

						this.state = null;

						return new Token(numberLiteralType, buildingToken.toStringAndClear());
					}

					this.line++;

					return new Token(Type.NEW_LINE, null);
				}

				if (insideComment) {
					continue;
				}

				if (ch == '"' && lastChar != '\\' && state != IN_CHAR_LITERAL) {
					if (state == IN_STRING_LITERAL) {
						this.state = null;

						return new Token(Type.STRING_LITERAL, buildingToken.toStringAndClear());
					}

					if (state == IN_NUMBER_LITERAL) {
						bye("Double quotes found while parsing a number literal");
					}

					if (state == IN_NAME_DEFINITION) {
						bye("Double quotes found while parsing a name definition");
					}

					this.state = IN_STRING_LITERAL;

					continue;
				}

				if (ch == '\'' && lastChar != '\\' && state != IN_STRING_LITERAL) {
					if (state == IN_CHAR_LITERAL) {
						this.state = null;

						return new Token(Type.CHAR_LITERAL, buildingToken.toStringAndClear());
					}

					if (state == IN_NUMBER_LITERAL) {
						bye("Single quote found while parsing a number literal");
					}

					if (state == IN_NAME_DEFINITION) {
						bye("Single quote found while parsing a name definition");
					}

					this.state = IN_CHAR_LITERAL;

					continue;
				}

				if (state == IN_NUMBER_LITERAL) {
					if (ch == 'b' && buildingToken.length() == 1 && buildingToken.charAt(0) == '0') {
						if (numberLiteralType == Type.DECIMAL_LITERAL) {
							this.numberLiteralType = Type.BINARY_LITERAL;

							buildingToken.clear();

							continue;
						}
						else {
							bye("Cannot re-define number literal type (already " + numberLiteralType + ')');
						}
					}

					if (ch == 'x' && buildingToken.length() == 1 && buildingToken.charAt(0) == '0') {
						if (numberLiteralType == Type.DECIMAL_LITERAL) {
							this.numberLiteralType = Type.HEXADECIMAL_LITERAL;

							buildingToken.clear();

							continue;
						}
						else {
							bye("Cannot re-define number literal type (already " + numberLiteralType + ')');
						}
					}

					boolean condition = false;

					if (numberLiteralType == Type.BINARY_LITERAL) {
						if (isLetter) {
							if (!CharSet.BINARY.contains(ch)) {
								bye("Invalid binary character \"" + ch + "\"");
							}
						}
						else {
							condition = !isDigit;
						}
					}
					else if (numberLiteralType == Type.DECIMAL_LITERAL) {
						if (isLetter) {
							bye("Letter found while parsing a number literal");
						}

						condition = !isDigit && ch != '.';
					}
					else if (numberLiteralType == Type.HEXADECIMAL_LITERAL) {
						if (isLetter) {
							if (!CharSet.HEXADECIMAL.contains(ch)) {
								bye("Invalid hexadecimal character \"" + ch + "\"");
							}
						}
						else {
							condition = !isDigit;
						}
					}

					if (condition) {
						back();

						this.state = null;

						return new Token(numberLiteralType, buildingToken.toStringAndClear());
					}
				}
				else if (state != IN_CHAR_LITERAL && state != IN_STRING_LITERAL) {
					if (ch == ' ' || ch == '\t') {
						if (state == IN_NAME_DEFINITION) {
							this.state = null;

							return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
						}

						continue;
					}

					if (state == IN_NAME_DEFINITION && !isLetter && !isDigit) {
						back();

						this.state = null;

						return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
					}

					if (!isLetter && !isDigit) {
						if (ch == ',') {
							return new Token(Type.COMMA, ch);
						}

						if (ch == KeywordDefinitions.FUNCTION_REFERENCE) {
							return new Token(Type.FUNC_REF, ch);
						}

						if (ch == KeywordDefinitions.COLON) {
							return new Token(Type.COLON, ch);
						}

						if (ch == KeywordDefinitions.GLOBAL_ACCESS) {
							return new Token(Type.GLOBAL_ACCESS, ch);
						}

						if (ch == KeywordDefinitions.OBJECT_ACCESS) {
							return new Token(Type.OBJECT_ACCESS, ch);
						}

						if (ch == '{') {
							return new Token(Type.OPEN_BRACE, ch);
						}

						if (ch == '[') {
							return new Token(Type.OPEN_BRACKET, ch);
						}

						if (ch == '(') {
							return new Token(Type.OPEN_PARENTHESE, ch);
						}

						if (ch == '}') {
							return new Token(Type.CLOSE_BRACE, ch);
						}

						if (ch == ']') {
							return new Token(Type.CLOSE_BRACKET, ch);
						}

						if (ch == ')') {
							return new Token(Type.CLOSE_PARENTHESE, ch);
						}

						if (ch == KeywordDefinitions.COMMENTER) {
							this.insideComment = true;

							continue;
						}

						return new Token(Type.OPERATOR, ch);
					}

					if (isDigit && state != IN_NAME_DEFINITION) {
						this.numberLiteralType = Type.DECIMAL_LITERAL;
						this.state = IN_NUMBER_LITERAL;
					}
					else if (isLetter && state != IN_NAME_DEFINITION) {
						this.state = IN_NAME_DEFINITION;
					}
				}

				if (lastChar == '\\') {
					buildingToken.append(StringWorker.unescape("\\" + ch));
					ch = 0;
				}
				else if (ch != '\\') {
					buildingToken.append(ch);
				}
			}
			finally {
				this.lastChar = ch;
			}
		}

		return null;
	}

	public void reRead(Token token) {
		reread.push(token);
	}

	protected void back() {
		this.position--;
	}

	protected void bye(String message) throws UnexpectedInputException {
		throw new UnexpectedInputException(origin.getScriptName(), currentLine(), message);
	}

	protected boolean canRead() {
		return position <= string.length();
	}

	protected char read() {
		if (position == string.length()) {
			this.line++;
			this.position++;

			return '\n';
		}

		return string.charAt(position++);
	}

	public enum State {
		IN_STRING_LITERAL,
		IN_NUMBER_LITERAL,
		IN_CHAR_LITERAL,
		IN_NAME_DEFINITION
	}
}