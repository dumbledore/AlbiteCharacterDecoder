/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.midlet.*;
import org.albite.io.decoders.AlbiteStreamReader;
import org.albite.io.decoders.Encodings;

/**
 * @author albus
 */
public class TestMidlet extends MIDlet implements Encodings {

    private static final String errorMsg = "API's result doesn't match java's "
            + "InputStreamReader results of the utf-8 version";

    public void startApp() {
        /*
         * start the tests
         */
        runTests();

        /*
         * exit the app
         */
        destroyApp(true);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }

    private void runTests() {
        System.out.println("Starting tests...");

        try {

            /*
             * Start of tests
             */

            /*
             * Assert the encodings strings are OK
             */
            for (int i = 0; i < ENCODINGS.length; i++) {
                assertEncodingAvailable(ENCODINGS[i]);
            }

            /*
             * Assert all aliases are OK
             */
            for (int i = 0; i < ALIASES.length; i++) {
                for (int j = 0; j < ALIASES[i].length; j++) {
                    assertEncodingAvailable(ALIASES[i][j]);
                }
            }

            /*
             * Try some unknown encodings
             */
            assertEncodingUnavailable(null);
            assertEncodingUnavailable("utf-16");
            assertEncodingUnavailable("utf-32");
            assertEncodingUnavailable("");
            assertEncodingUnavailable("xcp-123456789");

            /*
             * Testing decoders
             */
            test("ascii");

            test("utf-8");
            test("/test/res/orig/utf-8-bom.txt",
                    "/test/res/utf8/utf-8.txt", "utf-8");

            test("iso-8859-1");
            test("iso-8859-2");
            test("iso-8859-3");
            test("iso-8859-4");
            test("iso-8859-5");
            test("iso-8859-7");
            test("iso-8859-9");
            test("iso-8859-10");
            test("iso-8859-13");
            test("iso-8859-14");
            test("iso-8859-15");
            test("iso-8859-16");

            test("windows-1250");
            test("windows-1251");
            test("windows-1252");
            test("windows-1253");
            test("windows-1254");
            test("windows-1257");

            test("koi8-r");
            test("koi8-ru");
            test("koi8-u");

            /*
             * End of tests
             */

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        System.out.println("All tests completed successfully!");
    }

    private void test(final String encoding) throws Exception {
        test(
                "/test/res/orig/" + encoding + ".txt",
                "/test/res/utf8/" + encoding + ".txt",
                encoding);
    }

    private void test(
            final String origFile,
            final String utf8File,
            final String encoding) throws Exception {

        System.out.print("Testing " + encoding + "...");

        final InputStream origStream = getClass().getResourceAsStream(origFile);
        final InputStream utf8Stream = getClass().getResourceAsStream(utf8File);

        if (origStream == null) {
            throw new Exception("Missing file: " + origFile);
        }

        if (utf8Stream == null) {
            throw new Exception("Missing file: " + utf8File);
        }

        final StringBuffer originalBuffer   = new StringBuffer();
        final StringBuffer utf8Buffer       = new StringBuffer();

        {
            /*
             * Read the original file
             */
            AlbiteStreamReader reader =
                    new AlbiteStreamReader(origStream, encoding);

            int read;

            while ((read = reader.read()) != -1) {
                originalBuffer.append((char) read);
            }
        }

        {
            /*
             * Now, read the utf-8 file, using the official java reader
             */
            InputStreamReader reader =
                    new InputStreamReader(utf8Stream, "utf-8");

            int read;

            while((read = reader.read()) != -1) {
                utf8Buffer.append((char) read);
            }
        }

        /*
         * Compare the results
         */
        if (originalBuffer.length() != utf8Buffer.length()) {
            throw new Exception(errorMsg);
        }

        char[] original = new char[originalBuffer.length()];
        originalBuffer.getChars(0, originalBuffer.length(), original, 0);

        char[] utf8 = new char[utf8Buffer.length()];
        utf8Buffer.getChars(0, utf8Buffer.length(), utf8, 0);

        for (int i = 0; i < original.length; i ++) {
            if (original[i] != utf8[i]) {
            System.out.println("No match: " + original[i] + " : " + utf8[i]);
                throw new Exception(errorMsg);
            }
        }

        System.out.println("done.");
    }

    private void assertEncodingUnavailable(final String encoding)
            throws Exception {

        System.out.print("Testing rejection of `" + encoding + "`...");

        if (AlbiteStreamReader.encodingSupported(encoding)) {
            throw new Exception("Encoding " + encoding
                    + " reported avialable, though shouldn't be.");
        }

        System.out.println("done.");
    }

    private void assertEncodingAvailable(final String encoding)
            throws Exception {

        System.out.print("Testing availability of `" + encoding + "`...");

        if (!AlbiteStreamReader.encodingSupported(encoding)) {
            throw new Exception("Encoding " + encoding
                    + " reported unavialable, though should be.");
        }

        System.out.println("done.");
    }
}