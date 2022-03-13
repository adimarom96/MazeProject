package IO;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read(byte[] b) throws IOException {
        byte[] compress = in.readAllBytes();// read all the file - 24 of MetaData and the rest is counters.
        in.close();
        int rest = compress[24];
        int x;
        int[] arr;
        int index = 24;

        //copy the first 24 cells to b.
        System.arraycopy(compress, 0, b, 0, 24);

        // decompress the numbers by using 8 bit function and add them to b array.
        for (int i = 25; i < compress.length - rest; i++) {
            x = compress[i];
            if (x < 0)
                x += 256;
            arr = eightBit(x);
            for (int j = 0; j < 8; j++) {
                add(b, index, arr[j]);
                index ++;
                //index += 4; old
            }
        }

        // decompress the rest of the numbers and add them to b array.
        for (int i = 0; i < rest; i++) {
            add(b, index, compress[compress.length - rest + i]);
            //index += 4; old
            index++;
        }

        return 0;
    }

    /**
     * get a decimal number and convert it to 8 bit binary number
     *
     * @param x
     * @return
     */
    public int[] eightBit(int x) {
        String str = Integer.toBinaryString(x);
        while (str.length() < 8)
            str = "0" + str;
        int[] arr = new int[8];
        char[] ca = str.toCharArray();
        for (int j = 0; j < 8; j++) {
            arr[j] = Character.getNumericValue(ca[j]);
        }
        return arr;
    }

    /**
     * get a int number and insert it as 4 byte
     *
     * @param b
     * @param startIndex
     * @param value      (will get just one or zeros)
     */
    private void add(byte[] b, int startIndex, int value) {
        /*b[startIndex] = 0;
        b[startIndex + 1] = 0;
        b[startIndex + 2] = 0;
        b[startIndex + 3] = (byte) value; old  */
        b[startIndex] = (byte) value;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}