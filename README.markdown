AlbiteStreamReader provides the ability to read characters from encodings, not supported natively supported by Java's InputStreamReader:

1. UTF-8,
1. ASCII,
1. ISO-8859-1,
1. ISO-8859-2,
1. ISO-8859-3,
1. ISO-8859-4,
1. ISO-8859-5,
1. ISO-8859-7,
1. ISO-8859-9,
1. ISO-8859-10,
1. ISO-8859-13,
1. ISO-8859-14,
1. ISO-8859-15,
1. ISO-8859-16,
1. WINDOWS-1250,
1. WINDOWS-1251,
1. WINDOWS-1252,
1. WINDOWS-1253,
1. WINDOWS-1254,
1. WINDOWS-1257,
1. KOI8-R,
1. KOI8-RU,
1. KOI8-U

Using the API is almost the same like the `InputStreamReader`, with a few exceptions:

- creating an `AlbiteStreamReader` can throw an IOException, because it may need to skip the BOM of a UTF-8 stream
- a list of supported encodings can be obtained using `Encodings.ENCODINGS`
- most encodings have name aliases, see the `Encodings` interface

Here is an example of its usage, extracted from the test class:

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
