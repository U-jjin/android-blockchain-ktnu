package com.universeluvv.ktnuvoting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class GetTokenTask extends AsyncTask<String,String,String> {

    connectToEth connect = new connectToEth();



    public String encodeTransferData(String Address, BigInteger sum){
        Function function = new Function(
                "transfer", // 호출 할 메소드
                Arrays.asList(new Address(Address),new Uint256(sum)),
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {}));
        return FunctionEncoder.encode(function);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... strings) {
        Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        String privateKey = "8BEBA8BBBC0E8D1C3E06C3939F9E83B7D8F2E806FB23387D4BF76C68901BF48F";
        Credentials credentials = Credentials.create(privateKey);

        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(strings[0],sum);
        try{
            String txHash = manager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddr,
                    data,
                    BigInteger.ZERO).getTransactionHash();

            Log.i("txHash",txHash);
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                    web3,
                    TransactionManager.DEFAULT_POLLING_FREQUENCY,
                    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
            Log.i("txReceipt",txReceipt.getTransactionHash());
            result = "success";
        }catch (IOException e ){
            result = "fail";
        }catch (TransactionException e){
            result = "fail";
        }
        return result;
    }

}
