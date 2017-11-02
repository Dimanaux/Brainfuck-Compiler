

import java.io.*;
import java.util.Scanner;


class Brainfuck {
	public static void main(String[] args)
			throws IOException {
		try {
			compile(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	static void compile(String bFileName)
			throws IOException {
		Scanner scanner = new Scanner(new File(bFileName));

		String cFileName = bFileName + ".c";

		File cfile = new File(cFileName);
		if (!cfile.exists()) {
			try {
				cfile.createNewFile();
			} catch (Exception e) {
				return;
			}
		}

		PrintWriter cout = new PrintWriter(cfile.getAbsoluteFile());

		StringBuilder cCode = new StringBuilder(
			"#include <stdlib.h>\n"
			+ "#include <string.h>\n"
			+ "#include <stdio.h>\n\n"
			+ "int main(int argc, char* argv[])\n"
			+ "{\n"
			+ "\tint size = 30000;\n"
			+ "\tif (argc > 1)\n"
			+ "\t{\n"
			+ "\t\tsize = atoi(argv[1]);\n"
			+ "\t}\n"
			+ "\tchar* arr = (char*) malloc(size);\n"
			+ "\tmemset(arr, 0, size);"
			+ "\tchar* ptr = arr;\n"
		);

		StringBuilder bfCodeBuilder = new StringBuilder();

		while (scanner.hasNext()) {
			bfCodeBuilder.append(scanner.next());
		}

		String bfCode = bfCodeBuilder.toString();

		StringBuffer nedding = new StringBuffer("\t");


		// parsing ... not so interesting
		parse : for (int i = 0; i < bfCode.length(); i++) {
			switch (bfCode.charAt(i)) {
			case '>':
				cCode.append(nedding);
				cCode.append("++ptr;\n");
				break;

			case '<':
				cCode.append(nedding);
				cCode.append("--ptr;\n");
				break;

			case '+':
				cCode.append(nedding);
				cCode.append("++(*ptr);\n");
				break;

			case '-':
				cCode.append(nedding);
				cCode.append("--(*ptr);\n");
				break;

			case '.':
				cCode.append(nedding);
				cCode.append("putchar(*ptr);\n");
				break;

			case ',':
				cCode.append(nedding);
				cCode.append("*ptr = getchar();\n");
				break;

			case '[':
				cCode.append(nedding);
				cCode.append("while (*ptr)\n");
				cCode.append(nedding);
				cCode.append("{\n");
				nedding.append("\t");
				break;

			case ']':
				nedding.deleteCharAt(nedding.length() - 1);
				cCode.append(nedding);
				cCode.append("}\n");
				break;
			default:
				continue parse;
			}
		}

		cCode.append("\treturn 0;\n}\n");
		cout.print(cCode.toString());
		cout.close();

		Process proc = Runtime.getRuntime().exec(
			"gcc " + cFileName + " -o " + bFileName + ".exe"
		);
	}
}


