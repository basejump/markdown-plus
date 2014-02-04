package nineci.markdownplus;

public class Markup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        StringBuffer buf = new StringBuffer();
        char[] cbuf = new char[1024];
        java.io.Reader in = new java.io.InputStreamReader(System.in);
        try {
            int charsRead = in.read(cbuf);
            while (charsRead >= 0) {
                buf.append(cbuf, 0, charsRead);
                charsRead = in.read(cbuf);
            }
            System.out.println(new MarkdownPlus().markdown(buf.toString()));
        } catch (java.io.IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            System.exit(1);
        }
    }

}
