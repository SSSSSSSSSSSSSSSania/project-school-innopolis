package com.company;

import java.util.Arrays;

/**
 * Created by ivan on 17.08.18.
 */
public class ConsoleMessager {
    public void open(String s) {
        String end="----------------------------------------------------------------------------------- - -  -  -   -";
        end=end.substring(s.length());
        System.out.println("+-----------"+s+end);
    }

    public void cont(String s) {
        System.out.println("|   "+s);
    }

    public void close() {
        System.out.println("+============================================================================================== = =  =  =   =");
        System.out.println();
    }

    public void cont(String v, Object v1) {
        cont(v+": "+v1.toString());
    }
    public void cont(String v, Object[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, byte[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, long[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, String[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, int[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, boolean[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, float[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
    public void cont(String v, double[] v1) {
        cont(v+": "+ Arrays.toString(v1));
    }
}
