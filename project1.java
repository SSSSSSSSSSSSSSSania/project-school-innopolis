package com.company;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class project1 {

    public static void main(String[] args) throws Exception{
        // write your code here
        System.out.println("START!");
        //Web3j web3 = Web3j.build(new HttpService("https://kovan.infura.io/mew"));
        Web3j web3 = Web3j.build(new HttpService("https://kovan.infura.io/mew"));
        System.out.println("WEB3j IS READY!");


        String sm1addr="0x16039e45f59c46507ba4c1c8a40e829af281256a";
        String sm2addr="0x1286470032cc2729774f5cf966770b450825f104";


        String privkey="8e1a2ec8d8e729183e028207c068ec9a4655512017e456b301a18d5bbc350775";
        String pubkey="";

        BigInteger privateKeyInBT = new BigInteger(privkey, 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        BigInteger publicKeyInBT = aPair.getPublicKey();

        String sPublickeyInHex = publicKeyInBT.toString(16);
        pubkey=sPublickeyInHex;




        Credentials credentials = Credentials.create(privkey,pubkey);//WalletUtils.loadCredentials("cfvggkrubhcg","MezhBlockchain/keyMetamask.json");
        System.out.println("CONNECTED!");
        Sm1 sm1=Sm1.load(sm1addr,web3,credentials,big(1),big(8000000));
        System.out.println("SM1 IS READY!");
        Sm2 sm2=Sm2.load(sm2addr,web3,credentials,big(1),big(8000000));
        System.out.println("SM2 IS READY!");



        System.out.println("Event listening...");

        //Subscription subscription = web3.transactionObservable().subscribe();
        int oldl=updateEvents(web3,sm1,sm1addr).size()-1;////remove -1!!!!!!!!!!!!
        while(true) {
            List<FuncParams> data = updateEvents(web3,sm1,sm1addr);
            if(data.size()>oldl) {
                oldl=data.size();
                FuncParams last = data.get(data.size() - 1);
                System.out.println("+-----------Event input------------------------------------------------------------------------ - -  -  -   -");
                System.out.println("|   Transaction hash: " + last.txHash);
                System.out.println("|   Address: " + last.addr);
                System.out.println("|   Data: " + Arrays.toString(last.data));
                System.out.println("+============================================================================================== = =  =  =   =");
                System.out.println();
                Thread.sleep(1000);
                System.out.println("+-----------Message---------------------------------------------------------------------------- - -  -  -   -");
                System.out.println("|   Data transfer begin!");
                System.out.println("|   Transferring data to: "+sm2addr+"...");
                try {
                    Thread.sleep(100);
                    sm2.PassMesage(last.addr, last.data,hexStringToByteArray(last.txHash)).send();
                    Thread.sleep(100);
                    System.out.println("|   Transaction finished!");
                    System.out.println("|   Ready!");
                    System.out.println("+============================================================================================== = =  =  =   =");
                    System.out.println();
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("|   Error: "+e.getMessage()+"!");
                    System.out.println("|   Ready!");
                    System.out.println("+============================================================================================== = =  =  =   =");
                    System.out.println();
                }

            }
            Thread.sleep(5000);
        }
        //System.out.println(web3.ethGetLogs(filter).send().getLogs());
        /*web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
            @Override
            public void call(Log log) {
                System.out.println("log.toString(): " +  log.toString());
            }
        });*/
        //System.out.println(web3.ethGetLogs(filter).send().getLogs().toArray());

        //TransactionManager tm=;
        //Sum sumc=Sum.deploy(web3,credentials,big(1),big(200000)).send();

    }

    private static List<FuncParams> updateEvents(Web3j web3,Sm1 sm1,String sm1addr) throws IOException {
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

    private static BigInteger big(int i) {
        return new BigInteger(Integer.toString(i));
    }
}
