package com.universeluvv.ktnuvoting;

import android.os.AsyncTask;
import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KnutToken extends AsyncTask<String,String,String> {
    connectToEth connect = new connectToEth();
    Web3j web3 = connect.ConnectToTestNet();

    // Token Transfer Fuction
    public String encodeTransferData(String Address, BigInteger sum){
        Function function = new Function(
                "transfer", // 호출 할 메소드
                Arrays.asList(new Address(Address),new Uint256(sum)),
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {}));
        return FunctionEncoder.encode(function);
    }

    @Override
    protected String doInBackground(String... strings) {
        System.out.println(strings[0]);
        System.out.println(strings[1]);
        //Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        Credentials credentials = Credentials.create(strings[0]);

        //RawTransactionManager manager     = new RawTransactionManager(web3,credentials);
        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        //BigInteger sum = BigInteger.valueOf("1000000000000000000");
        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(strings[1],sum);

        try{
            String txHash = manager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddr,
                    data,
                    BigInteger.ZERO).getTransactionHash();


            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                    web3,
                    TransactionManager.DEFAULT_POLLING_FREQUENCY,
                    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
            result = txReceipt.getTransactionHash();
           // Log.i("txReceipt",txReceipt.getTransactionHash());
        }catch (IOException e ){
            result = "fail";
            return result;
        }catch (TransactionException e){
            result = "fail";
            return result;
        }
        return result;
    }

    public String Voting (String privateKey,String toAddr){
        Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        Credentials credentials = Credentials.create(privateKey);

        //RawTransactionManager manager     = new RawTransactionManager(web3,credentials);
        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        //BigInteger sum = BigInteger.valueOf("1000000000000000000");
        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(toAddr,sum);

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
            result = txReceipt.getTransactionHash();
            Log.i("txReceipt",txReceipt.getTransactionHash());
        }catch (IOException e ){
            result = "fail";
            return result;
        }catch (TransactionException e){
            result = "fail";
            return result;
        }
        return result;
    }

    public String encodeBalanceOfData(String Address){
        Function function = new Function(
                "balanceOf",
                Arrays.asList(new Address(Address)),
                Arrays.asList(new org.web3j.abi.TypeReference<Uint256>() {}));
        return FunctionEncoder.encode(function);
    }

    public String balanceOf(String Addr){
        //Web3j web3 = connect.ConnectToTestNet();
        String txdata = encodeBalanceOfData(Addr);
        String contract = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        String result = null;
        Address[] addressInput = new Address[]{new Address(Addr)};
        Function function = new Function(
                "balanceOf",
                Arrays.<Type>asList(addressInput),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        try {
            EthCall ethCall = web3.ethCall(
                    Transaction.createEthCallTransaction(Addr, contract, txdata),
                    DefaultBlockParameterName.LATEST).sendAsync().get();

            if (ethCall == null){
                Log.i("검사","null 반환");
            } else if(ethCall.getValue() == null){
                result = "0";
                return result;
            }else {
                List<Type> someTypes = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                Log.i("토큰 : ", ethCall.getResult());
                for (int i = 0; i < someTypes.size(); i++) {
                    System.out.println("" + someTypes.get(i).getValue());
                    result = parseDecimal(someTypes.get(i).getValue().toString());
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return result;
    }

    public String parseDecimal(String tokenBalance){
        if(tokenBalance.equals("0")){
            return tokenBalance;
        }
        else if (tokenBalance.length() == 19){
            return tokenBalance.substring(0,1);
        }else if (tokenBalance.length() == 20){
            return tokenBalance.substring(0,2);
        }else if (tokenBalance.length() == 21){
            return tokenBalance.substring(0,3);
        }else
            return null;
    }


}
