import java.io.*;
import java.nio.charset.Charset;
import java.util.InputMismatchException;

public class ProblemI {
    public static int BOUND =  2 * 100_000_000 + 1;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextIntInLine();
        in.goToNextLine();

        int xL = BOUND;
        int yL = BOUND;

        int xR = -BOUND;
        int yR = -BOUND;

        for (int i = 0; i < n; i++) {
            int xI = in.nextIntInLine();
            int yI = in.nextIntInLine();
            int hI = in.nextIntInLine();
            in.goToNextLine();

            xL = Integer.min(xL, xI - hI);
            yL = Integer.min(yL, yI - hI);

            xR = Integer.max(xR, xI + hI);
            yR = Integer.max(yR, yI + hI);
        }

        int h = ceilDivision(Integer.max(xR - xL, yR - yL), 2);
        int x = (xL + xR) / 2;
        int y = (yL + yR) / 2;
        System.out.printf("%d %d %d", x, y, h);
    }

    public static int ceilDivision(int a, int b) {
        return (a + b - 1) / b;
    }

    public interface Checker {
        boolean isSuitable(int codePoint);
    }

    public static class Scanner {

        private final Reader in;

        private Checker checker = codePoint -> !Character.isWhitespace(codePoint);

        private String token = "";

        private boolean EOL = false;
        private boolean EOF = false;

        public Scanner(InputStream in) {
            this.in = new BufferedReader(new InputStreamReader(in));
        }

        public Scanner(InputStream in, Checker checker) {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.checker = checker;
        }

        public Scanner(String fileName, String charsetName) throws IOException {
            in = new BufferedReader(new FileReader(fileName, Charset.forName(charsetName)));
        }

        public Scanner(String fileName, String charsetName, Checker checker) throws IOException {
            in = new BufferedReader(new FileReader(fileName, Charset.forName(charsetName)));
            this.checker = checker;
        }

        private String readToken() {
            if (token.isEmpty() && !EOL && !EOF) {
                StringBuilder tokenBuilder = new StringBuilder();
                boolean isToken = false;
                try {
                    while (!EOF && !EOL) {
                        int codePoint = in.read();
                        boolean isTokenChar = checker.isSuitable(codePoint);
                        if (codePoint == -1) {
                            EOF = true;
                            close();
                        } else if ((char) codePoint == '\n') {
                            EOL = true;
                        } else if ((char) codePoint == '\r') {
                            in.mark(1);
                            if ((char) in.read() != '\n') {
                                in.reset();
                            }
                            EOL = true;
                        } else if (isTokenChar) {
                            tokenBuilder.append((char) codePoint);
                            isToken = true;
                        } else if (isToken) {
                            break;
                        }
                    }
                    return tokenBuilder.toString();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    close();
                }
            }
            return token;
        }

        public boolean hasNextInLine() {
            token = readToken();
            return !token.isEmpty();
        }

        public boolean hasNextIntInLine() {
            token = readToken();
            try {
                if (token.toLowerCase().startsWith("0x")) {
                    Integer.parseUnsignedInt(token.substring(2), 16);
                } else {
                    Integer.parseInt(token);
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public boolean hasNextLine() {
            return !EOF;
        }

        public String nextInLine() {
            token = readToken();
            if (!hasNextInLine()) {
                if (EOF) {
                    throw new InputMismatchException("reading after EOF");
                } else if (EOL) {
                    throw new InputMismatchException("reading in ended line");
                }
            }
            String next = token;
            token = "";
            return next;
        }

        public int nextIntInLine() throws NumberFormatException {
            token = readToken();
            int nextInt;
            if (token.toLowerCase().startsWith("0x")) {
                nextInt = Integer.parseUnsignedInt(token.substring(2), 16);
            } else {
                nextInt = Integer.parseInt(token);
            }
            token = "";
            return nextInt;
        }

        public void goToNextLine() {
            EOL = false;
        }

        public void close() {
            try {
                in.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}