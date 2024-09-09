// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));
		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}
	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	public Token curToken() {
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;
		try {
			line = sourceFile.readLine();
			if (line == null) {
				sourceFile.close();
				sourceFile = null;
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// -- Must be changed in part 1:

		// Hvis linja er tom legges det til eof-token, og eventuelle dedent tokens
		if (line == null) {
			for (int i : indents) {
				if (i > 0) {
					curLineTokens.add(new Token(TokenKind.dedentToken, curLineNum()));
				}
			}
			curLineTokens.add(new Token(TokenKind.eofToken));
		}

		// Hvis linja ikke er tom leses den teng for tegn
		else if (line != null) {

			// Innledende TAB-er oversettes til riktig antall blanke
			line = expandLeadingTabs(line);

			// ignorerer linja hvis det bare er blanke eller den er tom
			if (line.isEmpty() || line.length() == spacesCount(line)) {
				return;
			}

			// ignorerer linja hvis hele linja er en kommentar
			if (line.charAt(findIndent(line)) == '#') {
				return;
			}

			// beregner indentering (gjøre om til en funksjon for leselighet?)
			int indentCount = findIndent(line); // teller antall innledende blanke og setter det til indentCount
			if (indentCount > indents.peek()) { // dersom antall innledende blanke på nåværende linje er MER enn den
												// foregående linja (eller mer enn 0 hvis det er første linje):
				indents.push(indentCount); // legger til antall innledende blanke i indents
				curLineTokens.add(new Token(TokenKind.indentToken, curLineNum()));
			}
			while (indentCount < indents.peek()) { // dersom innlendende blanke er færre enn forrige linje...:
				indents.pop(); // ...legges til riktig antall indents i forhold til forrige linjes indents
				curLineTokens.add(new Token(TokenKind.dedentToken, curLineNum()));
			}
			if (indentCount != indents.peek()) { // kan kanskje droppes??
				scannerError("indent error");
			}

			// -- Selve skanninga. Gå gjennom linja char for char -- //

			// initiering av div variabler
			String intLit = "";
			String floatLit = "";
			String stringLit = "";
			String name = "";

			int lineIndex = 0;

			// så lenge det er flere characters i linja..:
			while (lineIndex < line.length()) {
				char c = line.charAt(lineIndex);

				// blanke ignoreres
				if (Character.isWhitespace(c)) {
					lineIndex++;
				}

				// hvis det er et navn
				else if (isLetterAZ(c) || c == '_') { // lovlige tegn som start på navn
					name += c;
					lineIndex++;
					if (lineIndex >= line.length()) { // hvis det er slutten på linja
						Token t = new Token(TokenKind.nameToken, curLineNum());
						t.name = name;
						curLineTokens.add(t);
						name = "";
						break; // bryter den ut av navn-itereringa
					}
					while (isLetterAZ(line.charAt(lineIndex)) || line.charAt(lineIndex) == '_'
							|| isDigit(line.charAt(lineIndex))) {
						name += line.charAt(lineIndex);
						lineIndex++;
						if (lineIndex >= line.length()) { // hivs det er slutten på linja
							break; // bryter ut av navnitereringa
						}
					}

					// avhengig av hvilket navn lages riktig TokenKind - enten name eller
					// keyword-token
					switch (name) {
						case "and":
							Token andT = new Token(TokenKind.andToken, curLineNum());
							curLineTokens.add(andT);
							break;
						case "def":
							Token defT = new Token(TokenKind.defToken, curLineNum());
							curLineTokens.add(defT);
							break;
						case "elif":
							Token elifT = new Token(TokenKind.elifToken, curLineNum());
							curLineTokens.add(elifT);
							break;
						case "else":
							Token elseT = new Token(TokenKind.elseToken, curLineNum());
							curLineTokens.add(elseT);
							break;
						case "False":
							Token falseT = new Token(TokenKind.falseToken, curLineNum());
							curLineTokens.add(falseT);
							break;
						case "for":
							Token forT = new Token(TokenKind.forToken, curLineNum());
							curLineTokens.add(forT);
							break;
						case "global":
							Token globalT = new Token(TokenKind.globalToken, curLineNum());
							curLineTokens.add(globalT);
							break;
						case "if":
							Token ifT = new Token(TokenKind.ifToken, curLineNum());
							curLineTokens.add(ifT);
							break;
						case "in":
							Token inT = new Token(TokenKind.inToken, curLineNum());
							curLineTokens.add(inT);
							break;
						case "None":
							Token noneT = new Token(TokenKind.noneToken, curLineNum());
							curLineTokens.add(noneT);
							break;
						case "not":
							Token notT = new Token(TokenKind.notToken, curLineNum());
							curLineTokens.add(notT);
							break;
						case "or":
							Token orT = new Token(TokenKind.orToken, curLineNum());
							curLineTokens.add(orT);
							break;
						case "pass":
							Token passT = new Token(TokenKind.passToken, curLineNum());
							// passT.name = name;
							curLineTokens.add(passT);
							break;
						case "return":
							Token retT = new Token(TokenKind.returnToken, curLineNum());
							curLineTokens.add(retT);
							break;
						case "True":
							Token trueT = new Token(TokenKind.trueToken, curLineNum());
							curLineTokens.add(trueT);
							break;
						case "while":
							Token whileT = new Token(TokenKind.whileToken, curLineNum());
							curLineTokens.add(whileT);
							break;
						default:
							Token t = new Token(TokenKind.nameToken, curLineNum());
							t.name = name;
							curLineTokens.add(t);
					}
					name = "";
				}

				// hvis det er en intLit ller float lit somm ikke begynner på 0
				else if (isDigit(c) && c != '0') {
					intLit += c;
					lineIndex++;
					if (lineIndex >= line.length()) { // hvis tallet er mindre enn 10 det er slutten av linja legges
														// tallet til som token og man
														// breaker ut og
						Token t = new Token(TokenKind.integerToken, curLineNum());
						t.integerLit = Long.parseLong(intLit);
						curLineTokens.add(t);
						intLit = "";
						break;
					}

					// hvis integer er større enn 9
					while (isDigit(line.charAt(lineIndex)) && lineIndex <= line.length()) {
						intLit += line.charAt(lineIndex);
						lineIndex++;
						if (lineIndex >= line.length()) {
							break;
						}
					}

					// hivs integer er større enn 9 OG på slutten av linja: legger til integer og
					// breaker ut
					if (lineIndex >= line.length()) {
						Token t = new Token(TokenKind.integerToken, curLineNum());
						t.integerLit = Long.parseLong(intLit);
						curLineTokens.add(t);
						intLit = "";
						break;
					}

					// hvis det er en float
					if (line.charAt(lineIndex) == '.') {
						floatLit += intLit; // legger til eksisterende tall
						floatLit += line.charAt(lineIndex); // legger til '.'
						lineIndex++;
						if (lineIndex >= line.length() || !isDigit(line.charAt(lineIndex))) {
							scannerError("Illegal character '"+String.valueOf(line.charAt(lineIndex-1)+"'"));
						}
						while (isDigit(line.charAt(lineIndex)) && lineIndex < line.length()) {
							floatLit += line.charAt(lineIndex);
							lineIndex++;
							if (lineIndex >= line.length()) {
								break;
							}
						}
						Token f = new Token(TokenKind.floatToken, curLineNum());
						f.floatLit = Double.parseDouble(floatLit);
						curLineTokens.add(f);
						floatLit = "";
						intLit="";
					} else { // mindre enn 9 og ikke i slutten av linja
						Token t = new Token(TokenKind.integerToken, curLineNum());
						t.integerLit = Long.parseLong(intLit);
						curLineTokens.add(t);
						intLit = "";
					}

				}

				// hvis tallet er 0
				else if (c == '0') {
					intLit += c;
					lineIndex++;

					// hvis det er på slutten av linja
					if (lineIndex >= line.length()) {
						Token z = new Token(TokenKind.integerToken, curLineNum());
						z.integerLit = Integer.parseInt(intLit);
						curLineTokens.add(z);
						intLit = "";
						break;
					}

					// hvis det er en alene 0 eller har et tegn som ikke er et tall eller punktum
					// bakseg
					if (Character.isWhitespace(line.charAt(lineIndex))
							|| (!isDigit(line.charAt(lineIndex)) && line.charAt(lineIndex) != '.')) {
						Token z = new Token(TokenKind.integerToken, curLineNum());
						z.integerLit = Integer.parseInt(intLit);
						curLineTokens.add(z);
						intLit = "";
					}
					// hvis det er en float som begynner på 0
					else if (line.charAt(lineIndex) == '.') {
						floatLit += intLit; // legger til eksisterende tall
						floatLit += line.charAt(lineIndex); // legger til '.'
						lineIndex++;
						if (lineIndex >= line.length()) {
							scannerError("Illegal character '"+String.valueOf(line.charAt(lineIndex-1)+"'"));
						}
						while (isDigit(line.charAt(lineIndex)) && lineIndex < line.length()) {
							floatLit += line.charAt(lineIndex);
							lineIndex++;
							if (lineIndex >= line.length()) {
								break;
							}
						}
						Token f = new Token(TokenKind.floatToken, curLineNum());
						f.floatLit = Double.parseDouble(floatLit);
						curLineTokens.add(f);
						floatLit = "";
					} else {
						// mindre enn 9 og ikke i slutten av linja
						Token t = new Token(TokenKind.integerToken, curLineNum());
						t.integerLit = Long.parseLong(intLit);
						curLineTokens.add(t);
						intLit = "";
					}

				}

				// ulovlig float
				else if (line.charAt(lineIndex) == '.') {

					// punktum uten tall foran
					if (lineIndex + 1 < line.length()) {
						if (isDigit(line.charAt(lineIndex + 1))) {
							scannerError("Illegal character '"+String.valueOf(line.charAt(lineIndex)+"'"));
						}
					}

					else {
							scannerError("Illegal character '"+String.valueOf(line.charAt(lineIndex)+"'"));
					}
				}

				// hvis det er en delimiter eller operator
				else if (c == ':') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.colonToken, curLineNum()));
				} else if (c == ',') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.commaToken, curLineNum()));
				} else if (c == '=') {
					lineIndex++;
					if (line.charAt(lineIndex) == '=') {
						lineIndex++;
						curLineTokens.add(new Token(TokenKind.doubleEqualToken, curLineNum()));
					} else {
						curLineTokens.add(new Token(TokenKind.equalToken, curLineNum()));
					}
				} else if (c == '{') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.leftBraceToken, curLineNum()));
				} else if (c == '[') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.leftBracketToken, curLineNum()));
				} else if (c == '(') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.leftParToken, curLineNum()));
				} else if (c == '}') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.rightBraceToken, curLineNum()));
				} else if (c == ']') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.rightBracketToken, curLineNum()));
				} else if (c == ')') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.rightParToken, curLineNum()));
				} else if (c == ';') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.semicolonToken, curLineNum()));
				}

				// hvis det er en operator
				else if (c == '*') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.astToken, curLineNum()));
				} else if (c == '>') {
					lineIndex++;
					if (line.charAt(lineIndex) == '=') {
						lineIndex++;
						curLineTokens.add(new Token(TokenKind.greaterEqualToken, curLineNum()));
					} else {
						curLineTokens.add(new Token(TokenKind.greaterToken, curLineNum()));
					}
				} else if (c == '<') {
					lineIndex++;
					if (line.charAt(lineIndex) == '=') {
						lineIndex++;
						curLineTokens.add(new Token(TokenKind.lessEqualToken, curLineNum()));
					} else {
						curLineTokens.add(new Token(TokenKind.lessToken, curLineNum()));
					}
				} else if (c == '-') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.minusToken, curLineNum()));
				} else if (c == '%') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.percentToken, curLineNum()));
				} else if (c == '+') {
					lineIndex++;
					curLineTokens.add(new Token(TokenKind.plusToken, curLineNum()));
				} else if (c == '/') {
					lineIndex++;
					if (line.charAt(lineIndex) == '/') {
						lineIndex++;
						curLineTokens.add(new Token(TokenKind.doubleSlashToken, curLineNum()));
					} else {
						curLineTokens.add(new Token(TokenKind.slashToken, curLineNum()));
					}
				} else if (c == '!') {
					lineIndex++;
					if (line.charAt(lineIndex) == '=') {
						lineIndex++;
						curLineTokens.add(new Token(TokenKind.notEqualToken, curLineNum()));
					}
				} else if (c == '\'') {
					boolean fnuttSlutt = false;
					while (!fnuttSlutt) {
						lineIndex++;
						if (lineIndex >= line.length()) {
							scannerError("String literal not terminated");
						}

						while (line.charAt(lineIndex) != '\'') {
							if (lineIndex >= line.length()) {
								scannerError("String literal not terminated");
							}
							stringLit += line.charAt(lineIndex);
							lineIndex++;
							if (lineIndex >= line.length()) {
								scannerError("String literal not terminated");
							}
						}

						if (line.charAt(lineIndex) == '\'') {
							fnuttSlutt = true;
						}

					}
					Token t = new Token(TokenKind.stringToken, curLineNum());
					t.stringLit = stringLit;
					curLineTokens.add(t);
					stringLit = "";

					if (lineIndex < line.length()) {
						lineIndex++;
					} else {
						break;
					}
				} else if (c == '"') {
					boolean fnuttSlutt = false;
					while (!fnuttSlutt) {
						lineIndex++;
						if (lineIndex >= line.length()) {
							scannerError("String literal not terminated");
						}
						while (line.charAt(lineIndex) != '"') {
							if (lineIndex >= line.length()) {
								scannerError("String literal not terminated");
							}
							stringLit += line.charAt(lineIndex);
							lineIndex++;
							if (lineIndex >= line.length()) {
								scannerError("String literal not terminated");
							}
						}

						if (line.charAt(lineIndex) == '"') {
							fnuttSlutt = true;
						}

					}
					Token t = new Token(TokenKind.stringToken, curLineNum());
					t.stringLit = stringLit;
					curLineTokens.add(t);
					stringLit = "";

					if (lineIndex < line.length()) {
						lineIndex++;
					} else {
						break;
					}
				} else if (c == '#') {
					break;
				} else {
					scannerError("illegal character: " + line.charAt(lineIndex));
				}
			}

			// Terminate line:
			curLineTokens.add(new Token(newLineToken, curLineNum()));
		}

		for (Token t : curLineTokens)
			Main.log.noteToken(t);
	}

	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int spacesCount(String s) {
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				n++;
			}
		}
		return n;
	}

	private int findIndent(String s) {
		int indent = 0;

		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	private String expandLeadingTabs(String s) {
		// -- Must be changed in part 1:

		int n = 0; // initierer int-variabel som holder på ANTALL riktig blanke
		String riktigBlanke = ""; // initierer streng-variabel som holder på selve blanke

		for (int i = 0; i < s.length(); i++) { // itererer gjennom hvert element i linja
			if (s.charAt(i) == ' ') { // hvis 'space':
				riktigBlanke += " "; // legger til ett space
				n++; // øker riktig antall
			} else if (s.charAt(i) == '\t') { // hvis 'TAB':
				int m = 4 - (n % 4); // beregner riktig antall blanke
				for (int j = 0; j < m; j++) {
					riktigBlanke += " "; // øker blank-strengen med korrekt antall blanke-tegn som erstatter TAB
				}
				n += m; // øker n med riktig antall
			} else { // avbryt ved første tegn som ikke er blank
				// System.out.println("ferdig. riktig antall blanke er : "+n);
				riktigBlanke += s.substring(i); // legger til resten av tekstlinja fra fila fra og med første tegn som
												// ikke er blank
				// System.out.println(riktigBlanke);
				break;
			}
		}
		return riktigBlanke;
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		switch(k){
			case lessToken:
			case greaterToken:
			case doubleEqualToken:
			case greaterEqualToken:
			case lessEqualToken:
			case notEqualToken:
				return true;
			default:
				return false;
		}

	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		switch(k){
			case plusToken:
			case minusToken:
				return true;
			default:
				return false;
		}
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		switch(k){
            case astToken:
            case slashToken:
            case percentToken:
            case doubleSlashToken:
                return true;
            default:
				return false;
		}
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		switch(k){
			case plusToken:
			case minusToken:
				return true;
			default:
				return false;
		}
	}

	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}
