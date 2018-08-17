package com.company;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;

public class ContractInteract {

    public static void main(String[] args) throws Exception{
        // write your code here
        Web3j web3 = Web3j.build(new HttpService("https://kovan.infura.io/mew"));


        String contractAddr="0x23b79cc0a7eaecb6b901f1777093af52709c6baa";
        String privkey="8e1a2ec8d8e729183e028207c068ec9a4655512017e456b301a18d5bbc350775";
        String pubkey="";

        BigInteger privateKeyInBT = new BigInteger(privkey, 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        BigInteger publicKeyInBT = aPair.getPublicKey();

        String sPublickeyInHex = publicKeyInBT.toString(16);
        pubkey=sPublickeyInHex;




        Credentials credentials = Credentials.create(privkey,pubkey);//WalletUtils.loadCredentials("cfvggkrubhcg","MezhBlockchain/keyMetamask.json");
        //TransactionManager tm=;
        //Sum sumc=Sum.deploy(web3,credentials,big(1),big(200000)).send();
        Sum sumc=Sum.load(contractAddr,web3,credentials,big(1),big(200000));
        //System.out.println(sumc.getContractAddress());
        //Sum sumc=Sum.load("",web3,credentials,big(1),big(200000));
        sumc.setA(big(10)).send();
        sumc.setB(big(5)).send();
        System.out.println(sumc.getSum().send().toString());
        System.out.println(sumc.getSumTwo(big(2)).send().toString());
    }

    private static BigInteger big(int i) {
        return new BigInteger(Integer.toString(i));
    }
}
