import java.io.*;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

interface Brainfuck {
    static void main(String[] args) throws IOException {
        compile(args[0]);
    }

    Map<Character, String> brainFuckToC = new TreeMap<Character, String>() {{
        put('>', "++ptr;");
        put('<', "--ptr;");
        put('+', "++(*ptr);");
        put('-', "--(*ptr);");
        put('.', "putchar(*ptr);");
        put(',', "*ptr = getchar();");
        put('[', "\nwhile (*ptr) {");
        put(']', "}\n");
    }};

    static void compile(String brainFuckSourceFileName) throws IOException {
        Scanner sourceReader = new Scanner(new File(brainFuckSourceFileName));

        String cFileName = brainFuckSourceFileName + ".c";

        File cfile = new File(cFileName);
        cfile.createNewFile();

        StringBuilder brainFuckSource = new StringBuilder();
        sourceReader.forEachRemaining(brainFuckSource::append);

        PrintWriter cout = new PrintWriter(cfile.getAbsoluteFile());

        StringBuilder cSource = new StringBuilder(
            "#include <stdlib.h>\n"
            + "#include <string.h>\n"
            + "#include <stdio.h>\n\n"
            + "int main(int argc, char* argv[]) {\n"
            + "int size = 30000;\n"
            + "if (argc > 1) size = atoi(argv[1]);\n"
            + "char* arr = (char*) malloc(size);\n"
            + "memset(arr, 0, size);\n"
            + "char* ptr = arr;\n\n"
        );

        for (int i = 0; i < brainFuckSource.length(); i++) {
            String cLine = brainFuckToC.get(brainFuckSource.charAt(i));
            cSource.append(cLine).append("\n");
        }

        cSource.append("\nreturn 0;\n}\n");
        cout.print(cSource.toString());
        cout.close();

        Process proc = Runtime.getRuntime().exec(new String[] {
                "gcc", cFileName, "-o", brainFuckSourceFileName + ".out"
        });
    }
}
