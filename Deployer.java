package com.company;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

/**
 * Created by ivan on 15.08.18.
 */
public class Deployer {
    public static void main(String[] args) throws Exception {
        ConsoleMessager cm=new ConsoleMessager();
        cm.open("Loading");
        cm.cont("Starting WEB3j...(1/2)");
        Web3j web3c = Web3j.build(new HttpService("https://kovan.infura.io/mew"));
        cm.cont("Starting WEB3j...(2/2)");
        Web3j web3e = Web3j.build(new HttpService("https://kovan.infura.io/mew"));
        cm.cont("Generating keys...");


        //String sm1addr="0x16039e45f59c46507ba4c1c8a40e829af281256a";
        //String sm2addr="0x1286470032cc2729774f5cf966770b450825f104";


        String privkey="8e1a2ec8d8e729183e028207c068ec9a4655512017e456b301a18d5bbc350775";
        String pubkey="";

        BigInteger privateKeyInBT = new BigInteger(privkey, 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        BigInteger publicKeyInBT = aPair.getPublicKey();

        String sPublickeyInHex = publicKeyInBT.toString(16);
        pubkey=sPublickeyInHex;



        cm.cont("Loading wallet...");
        Credentials credentials = Credentials.create(privkey,pubkey);
        cm.cont("Ready!");
        cm.close();
        cm.open("Deploy");
        cm.cont("Deploying begins!");
        cm.cont("Deploying cheap...(1/4)");
        Cheap cheap = Cheap.deploy(web3c,credentials,big(1),big(2000000)).send();
        cm.cont("cheap address",cheap.getContractAddress());
        cm.cont("Deploying sm1...(2/4)");
        Sm1 sm1 = Sm1.deploy(web3c,credentials,big(1),big(2000000),big(1)).send();
        cm.cont("sm1 address",sm1.getContractAddress());
        cm.cont("Deploying sm2...(3/4)");
        Sm2 sm2 = Sm2.deploy(web3e,credentials,big(1),big(2000000),big(1)).send();
        cm.cont("sm2 address",sm2.getContractAddress());
        cm.cont("Deploying eth...(4/4)");
        Eth eth = Eth.deploy(web3e,credentials,big(1),big(2000000)).send();
        cm.cont("eth address",eth.getContractAddress());
        cm.cont("OK!");
        cm.close();
        cm.open("Contract addresses");
        cm.cont("cheap",cheap.getContractAddress());
        cm.cont("sm1",sm1.getContractAddress());
        cm.cont("sm2",sm2.getContractAddress());
        cm.cont("eth",eth.getContractAddress());
        cm.close();
    }

    private static BigInteger big(int i) {
        return new BigInteger(Integer.toString(i));
    }
}
