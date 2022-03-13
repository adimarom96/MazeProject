package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class MyCompressorOutputStream extends OutputStream {

    public OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte[] barray) throws IOException {

        int row = ((barray[0] & 0xFF) << 24) | ((barray[1] & 0xFF) << 16) | ((barray[2] & 0xFF) << 8) | ((barray[3] & 0xFF));
        int col = ((barray[4] & 0xFF) << 24) | ((barray[5] & 0xFF) << 16) | ((barray[6] & 0xFF) << 8) | ((barray[7] & 0xFF));
        int amount = col * row;
        int rest = (amount ) % 8; // hold the value of the rest (e.g size 4*5=20 -> two numbers of 8 bit, and another 4 regualr numbers)
        int[] binaryArr = new int[8];
        byte[] matrix = Arrays.copyOfRange(barray, 27, barray.length); // hold just the part of the maze matrix
        int index = 0;

        int num = amount-rest;
        for (int i = 0; i < 24; i++) {
            out.write(barray[i]);
        }
        out.write((byte) rest); // writing the rest as the 24 byte

        for (int i = 0; i < num; i++) {  // run the actual times of 8bit numbers that we need to write without the rest
            binaryArr[index] = matrix[i * 4];
            if ((index+1)%8 == 0 && i != 0) { // insert every 8 number to array and convert the to decimal number
                toDec(binaryArr);
                index = 0;
                continue;
            }
            index++;
        }
        for (int i = barray.length - rest*4+3; i < barray.length; i+=4) { // wrting the rest of the numbers
            out.write(barray[i]);
        }
    }

    /**
     *  conveert 8 bit number to decimal number
     * @param arr of 8 numbers
     * @throws IOException
     */
    private void toDec(int[] arr) throws IOException {
        int x = 0;
        for (int i = 0; i < 8; i++) {
            x += Math.pow(arr[i] * 2, 7 - i);
        }
        if (arr[7] == 0)
            x -= 1;
        out.write(x);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
