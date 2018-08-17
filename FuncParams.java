package com.company;

import java.util.Arrays;

/**
 * Created by ivan on 16.08.18.
 */
public class FuncParams {
    String addr;
    String txHash;
    byte[] data;
    public boolean equals(FuncParams a){
        if(a.addr.equals(addr)&&a.txHash.equals(txHash)&& Arrays.equals(data,a.data)){
            return true;
        }
        return false;
    }
}
