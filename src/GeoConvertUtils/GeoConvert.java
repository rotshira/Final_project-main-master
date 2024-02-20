/*
 * fr.geo.convert package
 * A geographic coordinates converter.
 * Copyright (C) 2002 Johan Montagnat
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Johan Montagnat
 * johan@creatis.insa-lyon.fr
 */

package GeoConvertUtils;


//import java.io.*;
import java.util.Iterator;
import java.util.Vector;


/**
 * Main class to GeoConvertUtils geographic coordinates.
 */
public class GeoConvert {

	private Coordinates srccoord;
	private Coordinates dstcoord;

	/**
	 * read command line arguments and GeoConvertUtils coordinates from command line,
	 * file, stdinput, or launch a GUI.
	 */


	/**
	 * print usage and exit
	 */

	/**
	 * create GUI
	 */


	/**
	 * parse and translate a set of input strings with an awk-like syntax
	 */
	class Translator {

		private class Node {
			protected Vector children;

			public Node() {
				children = new Vector();
			}

			public String toString() {
				String res = "";
				for (int i = 0; i < children.size(); i++)
					res += children.elementAt(i).toString();
				return res;
			}

			protected int parseExpression(String format, int i) {
				int open = 1;
				int j = i + 1;
				while (open > 0 && j < format.length()) {
					switch (format.charAt(j)) {
						case '{':
							if (format.charAt(j - 1) != '\\')
								open++;
							break;
						case '}':
							if (format.charAt(j - 1) != '\\')
								open--;
							break;
					}
					if (open > 0)
						j++;
				}
				if (open > 0) {
					System.err.println("Unbalanced bracket in input format string");
					return -1;
				}
				return j;
			}

			protected int parseField(String format, int i) {
				if (format.charAt(i) != '$' || format.charAt(i + 1) != '{')
					return -1;
				int j = i + 2;
				while (j < format.length() &&
						(format.charAt(j) != '}' || format.charAt(j - 1) == '\\'))
					j++;
				if (format.charAt(j) != '}') {
					System.err.println("Unclosed field in input format string");
					return -1;
				}
				return j;
			}
		}

		private class Value extends Node {
			public static final int STRING = 0, INT = 1, DOUBLE = 2;
			protected int kind;
			String str;
			double d;
			int i;

			public Value(String s) {
				try {
					d = Double.parseDouble(s);
					try {
						i = Integer.parseInt(s);
						if ((double) i == d)
							kind = INT;
						else
							kind = DOUBLE;
					} catch (NumberFormatException e) {
						kind = DOUBLE;
					}
				} catch (NumberFormatException e) {
					kind = STRING;
					str = new String(s);
				}
			}

			public Value(String s, int k) {
				kind = k;
				try {
					switch (kind) {
						case STRING:
							str = new String(s);
							break;
						case DOUBLE:
							d = Double.parseDouble(s);
							break;
						case INT:
							i = Integer.parseInt(s);
							break;
					}
				} catch (NumberFormatException e) {
					kind = STRING;
					str = new String(s);
				}
			}


			public String toString() {
				switch (kind) {
					case STRING:
						return str;
					case DOUBLE:
						return String.valueOf(d);
					case INT:
						return String.valueOf(i);
				}
				return "";
			}
		}

		private class Expression extends Node {
			public static final int SUM = 0, DIF = 1, MUL = 2, DIV = 3;
			protected int kind;

			public Expression(Root root, String s) {
				int i = 0;
				if (s.charAt(i) == '{') {
					int j = parseExpression(s, i);
					if (j == -1)
						invalid(s);
					children.add(new Expression(root, s.substring(1, j)));
					i = j + 1;
				} else if (s.charAt(i) == '$') {
					int j = parseField(s, i);
					if (j == -1)
						invalid(s);
					children.add(new Value(root.get(s.substring(2, j)), Value.STRING));
					i = j + 1;
				} else {
					while (i < s.length() && s.charAt(i) != '+' && s.charAt(i) != '-' &&
							s.charAt(i) != '*' && s.charAt(i) != '/')
						i++;
					children.add(new Value(s.substring(0, i), Value.DOUBLE));
				}
				if (i >= s.length())
					invalid(s);
				switch (s.charAt(i)) {
					case '+':
						kind = SUM;
						break;
					case '-':
						kind = DIF;
						break;
					case '*':
						kind = MUL;
						break;
					case '/':
						kind = DIV;
						break;
					default:
						invalid(s);
						break;
				}
				i++;
				if (s.charAt(i) == '{') {
					int j = parseExpression(s, i);
					if (j == -1)
						invalid(s);
					children.add(new Expression(root, s.substring(i + 1, j)));
				} else if (s.charAt(i) == '$') {
					int j = parseField(s, i);
					if (j == -1)
						invalid(s);
					children.add(new Value(root.get(s.substring(i + 2, j)), Value.STRING));
				} else {
					children.add(new Value(s.substring(i), Value.DOUBLE));
				}
			}

			private void invalid(String s) {
				System.err.println("Invalid expression: " + s);
				System.exit(-1);
			}

			public String toString() {
				try {
					double d1 = Double.parseDouble(children.elementAt(0).toString());
					double d2 = Double.parseDouble(children.elementAt(1).toString());
					switch (kind) {
						case SUM:
							return String.valueOf(d1 + d2);
						case DIF:
							return String.valueOf(d1 - d2);
						case MUL:
							return String.valueOf(d1 * d2);
						case DIV:
							return String.valueOf(d1 / d2);
					}
				} catch (NumberFormatException e) {
	/*System.err.println("Invalid expression. " +
			   children.elementAt(0).toString() +
			   " " + kind + " " +
			   children.elementAt(1).toString());
			   System.exit(-1);*/
					return "error";
				}
				return "";
			}
		}


		private class Root extends Node {
			String fields[][];

			public String get(String id) {
				try {
					int i, j;
					int n = 0;
					int m;
					boolean all = false;
					for (i = 0; i < id.length(); i++) {
						if (id.charAt(i) == ':') {
							i = id.length();
							break;
						}
						if (id.charAt(i) == '.') {
							n = Integer.parseInt(id.substring(0, i)) - 1;
							i++;
							if (i >= id.length())
								return "";
							if (id.charAt(i) == '>') {
								all = true;
								i++;
								if (i >= id.length())
									return "";
							}
							break;
						}
					}
					if (i >= id.length())
						i = 0;
					for (j = i + 1; j < id.length(); j++) {
						if (id.charAt(j) == ':')
							break;
					}
					m = Integer.parseInt(id.substring(i, j)) - 1;

					String res = "";
					;
					if (n >= 0 && n < fields.length && m >= 0 && m < fields[n].length) {
						if (all) {
							while (++m < fields[n].length)
								res += fields[n][m];
						} else
							res = fields[n][m];
					}

					if (res.equals("") && j < id.length() && id.charAt(j) == ':')
						return id.substring(j + 1);
					else
						return res;
				} catch (NumberFormatException e) {
					return "";
				}
			}


			public Root(String format, String f[][]) {
				fields = f;
				int i = 0;
				StringBuffer current = new StringBuffer();
				while (i < format.length()) {
					if (format.charAt(i) == '\\') {
						if (++i < format.length()) {
							switch (format.charAt(i)) {
								case '\\':
									current.append('\\');
									break;
								case 't':
									current.append('\t');
									break;
								case 'n':
									current.append('\n');
									break;
								case '{':
									current.append('{');
									break;
								case '}':
									current.append('}');
									break;
								default:
									current.append('\\');
									current.append(format.charAt(i));
									break;
							}
						} else {
							--i;
							current.append('\\');
						}
					} else if (format.charAt(i) == '{') {
						current = addChild(current);
						int j = parseExpression(format, i);
						if (j == -1)
							System.exit(-1);
						children.add(new Expression(this, format.substring(i + 1, j)));
						i = j;
					} else if (format.charAt(i) == '$') {
						if (++i < format.length()) {
							if (format.charAt(i) == '{') {
								current = addChild(current);
								int j = parseField(format, i - 1);
								children.add(new Value(get(format.substring(i + 1, j)),
										Value.STRING));
								i = j;
							} else {
								current.append('$');
								--i;
							}
						} else
							current.append('$');
					} else {
						current.append(format.charAt(i));
					}
					i++;
				}
				addChild(current);
			}


			protected StringBuffer addChild(StringBuffer child) {
				if (child.length() > 0) {
					children.add(new Value(child.toString(), Value.STRING));
					return new StringBuffer();
				} else
					return child;
			}
		}

		/**
		 * output string format
		 */
		private String format;


		/**
		 * build a new translator with given output format
		 *
		 * @param format output format string
		 */
		public Translator(String format) {
			this.format = format;
		}

		/**
		 * translates a set of input strings into output format
		 *
		 * @param in alternate array of input string and splitting regular
		 *           expressions
		 */
		public String translate(String in[]) {
			String fields[][] = new String[in.length / 2][];
			for (int i = 0; i < in.length; i += 2)
				fields[i / 2] = in[i].split(in[i + 1]);
			return new Root(format, fields).toString();
		}

		public String translate(String s, String sep) {
			String in[] = new String[2];
			in[0] = s;
			in[1] = sep;
			return translate(in);
		}

		public String translate(String s1, String sep1, String s2, String sep2) {
			String in[] = new String[4];
			in[0] = s1;
			in[1] = sep1;
			in[2] = s2;
			in[3] = sep2;
			return translate(in);
		}

		public String translate(String s1, String sep1, String s2, String sep2,
								String s3, String sep3) {
			String in[] = new String[6];
			in[0] = s1;
			in[1] = sep1;
			in[2] = s2;
			in[3] = sep2;
			in[4] = s3;
			in[5] = sep3;
			return translate(in);
		}
	}
}
