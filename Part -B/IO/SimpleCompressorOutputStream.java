package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleCompressorOutputStream extends OutputStream {
    public OutputStream out;

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte[] barray) throws IOException {
        // copy all the MetaData to the file.
        for (int i = 0; i < 24; i++) {
            out.write(barray[i]);
        }
        boolean flag = false; // true if we start with '1'.
        int zero_count = 0;
        int one_count = 0;
        List<Integer> compArr = new ArrayList<Integer>();// holds the counters of the amounts.
        barray[23] = -1;
        // we assume we start with 0, so if the first we see is '1' we add 0 to the array.
        if (barray[27] == 0)
            zero_count++;
        else {
            compArr.add(0);
            flag = true;
        }
        // because we in byte , we are checking only in jump of 4.
        for (int i = 27; i < barray.length + 1; i += 4) {
            if (barray[i] == 0) {
                if (barray[i - 4] == 0) {
                    zero_count++;
                } else if (barray[i - 4] == 1) {// if i'm 0 but the prev is 1
                    compArr.add(one_count);
                    one_count = 0;
                    zero_count = 1;
                }
            } else if (barray[i] == 1) {
                if (barray[i - 4] == 1 || flag) {//the last is also '1' or we start with '1' (flag)
                    one_count++;
                    flag = false;
                } else if (barray[i - 4] == 0) {// if i'm 1 but the prev is 0
                    compArr.add(zero_count);
                    one_count = 1;
                    zero_count = 0;
                }
            }
        }
        // add the last sequence
        if (zero_count != 0)
            compArr.add(zero_count);

        if (one_count != 0)
            compArr.add(one_count);

        // convert to bytes. if there are more than 255- handle it. than write it to the file.
        int x = 0;
        for (int i = 0; i < compArr.size(); i++) {
            if (compArr.get(i) >= 255) {
                x = compArr.get(i);
                while (x > 255) {
                    out.write((byte) 255);
                    out.write((byte) 0);
                    x -= 255;
                }
                out.write((byte) x);
                continue;
            }
            out.write((byte) compArr.get(i).intValue());
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}