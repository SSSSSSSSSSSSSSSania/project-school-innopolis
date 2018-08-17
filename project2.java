package com.company;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 17.08.18.
 */
public class project2 {
    public static void main(String[] args) {
        ConsoleMessager cm=new ConsoleMessager();
        cm.open("Starting");
        cm.cont("Loading WEB3j...");
        Web3j web3 = Web3j.build(new HttpService("https://kovan.infura.io/mew"));
        cm.cont("Initalizing...");





        String sm1addr="0x16039e45f59c46507ba4c1c8a40e829af281256a";
        String sm2addr="0x1286470032cc2729774f5cf966770b450825f104";


        String privkey="8e1a2ec8d8e729183e028207c068ec9a4655512017e456b301a18d5bbc350775";
        String pubkey="";

        BigInteger privateKeyInBT = new BigInteger(privkey, 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        BigInteger publicKeyInBT = aPair.getPublicKey();

        String sPublickeyInHex = publicKeyInBT.toString(16);
        pubkey=sPublickeyInHex;


        cm.close();
        String a="0x7b4b4d811cb805f936854bf9ea30e6b3b69ddb11";
        String b=bytesToHex(new byte[]{1,2,3,4,5});
        String c="0x6225C69789012CD85D1F5C3D22017123D8710F6325F9D774C2689CA7E1ADE83D";
        byte[] msg=hexStringToByteArray(packBytes(a,b,c));
        cm.open("Keccak256 output");
        cm.cont("A",a);
        cm.cont("B",b);
        cm.cont("C",c);
        cm.cont("Hex", bytesToHex(Hash.sha3(msg)));
        cm.close();
        /*Sign.SignatureData res= Sign.signMessage(msg,aPair);
        cm.open("Result");
        cm.cont("Address","0x7b4b4d811cb805f936854bf9ea30e6b3b69ddb11");
        cm.cont("Message",bytesToHex(msg));
        cm.cont("V",res.getV());
        cm.cont("R",bytesToHex(res.getR()));
        cm.cont("S",bytesToHex(res.getS()));
        cm.close();*/
        //System.out.println(Sign.signMessage(new byte[]{0,0,10},aPair));


    }

    private static String packBytes(String a, String b, String c) {
        return "0x"+a.substring(2)+b.substring(2)+c.substring(2);
    }

    private static List<FuncParams> updateEvents(Web3j web3, Sm1 sm1, String sm1addr) throws IOException {
        //String sm1addr=sm1.getContractAddress();
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, sm1addr);
        List<FuncParams> data=new ArrayList<>();
        List<EthLog.LogResult> logRes=web3.ethGetLogs(filter).send().getLogs();
        for (EthLog.LogResult t:logRes) {
            String txHash=getTXHash(t.toString());
            TransactionReceipt tr=web3.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().get();
            List<Sm1.RequessToPasMessageEventResponse> evs=sm1.getRequessToPasMessageEvents(tr);
            for (Sm1.RequessToPasMessageEventResponse te:
                    evs) {
                FuncParams fp=new FuncParams();
                fp.addr=te._eth;
                fp.txHash=txHash;
                fp.data=te._dat;
                data.add(fp);
            }
            //System.out.println(tr.toString());
        }
        return data;
    }

    private static String getTXHash(String s) {
        String st=s.substring(s.lastIndexOf("transactionHash=\'")+17);
        st=st.substring(0,st.indexOf("\'"));
        return st;
    }
    public static byte[] hexStringToByteArray(String s) {
        s=s.substring(2);
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return "0x"+new String(hexChars);
    }
    private static BigInteger big(int i) {
        return new BigInteger(Integer.toString(i));
    }
}
