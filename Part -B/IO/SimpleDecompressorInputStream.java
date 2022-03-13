package IO;

import java.io.IOException;
import java.io.InputStream;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in;

    // constructor
    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        // b is empty now.
        byte[] compress = in.readAllBytes();// read all the file - 24 of MetaData and the rest is counters.
        in.close();

        //copy the first 24 cells to b
        for (int i = 0; i < 24; i++) {
            b[i] = compress[i];
        }

        int bindex = 24;
        int times = 0;
        boolean addone = false;
        if (compress[24] == 0) { // starting with '1'
            addone = true;
            for (int i = 25; i < compress.length; i++) {
                times = compress[i];
                if (times < 0) {
                    times += 256;
                }
                for (int j = 0; j < times; j++) {
                    if (addone) {
                        add(b, bindex, 1);
                    } else {
                        add(b, bindex, 0);
                    }
//                    bindex += 4;
                    bindex++;
                }
                addone = !addone;
            }
        } else { // start with '0'
            addone = false;
            for (int i = 24; i < compress.length; i++) {
                times = compress[i];
                if (times < 0) {
                    times += 256;
                }
                for (int j = 0; j < times; j++) {
                    if (addone) {
                        add(b, bindex, 1);
                    } else {
                        add(b, bindex, 0);
                    }
                    //bindex += 4;
                    bindex++;
                }
                addone = !addone;
            }
        }
        return 0;
    }

    /**
     * function that convert to bytes.
     *
     * @param b          - the array to write in
     * @param startIndex - where to write
     * @param value      - 0/1
     */
    private void add(byte[] b, int startIndex, int value) {
        /*b[startIndex] = 0;
        b[startIndex + 1] = 0;
        b[startIndex + 2] = 0;
        b[startIndex + 3] = (byte) value;*/
        b[startIndex] = (byte) value;
    }
}