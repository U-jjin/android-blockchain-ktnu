package com.universeluvv.ktnuvoting;

import android.os.AsyncTask;
import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BalanceOfTask extends AsyncTask<String,Void,String> {
    connectToEth connect = new connectToEth();


    public String encodeBalanceOfData(String Address){
        Function function = new Function(
                "balanceOf",
                Arrays.asList(new Address(Address)),
                Arrays.asList(new org.web3j.abi.TypeReference<Uint256>() {}));
        return FunctionEncoder.encode(function);
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


    @Override
    protected String doInBackground(String... strings) {
        Web3j web3 = connect.ConnectToTestNet();
        String txdata = encodeBalanceOfData(strings[0]);
        String contract = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        String result = null;
        Address[] addressInput = new Address[]{new Address(strings[0])};
        Function function = new Function(
                "balanceOf",
                Arrays.<Type>asList(addressInput),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        try {
            EthCall ethCall = web3.ethCall(
                    Transaction.createEthCallTransaction(strings[0], contract, txdata),
                    DefaultBlockParameterName.LATEST).sendAsync().get();

            if (ethCall == null) {
                Log.i("검사", "null 반환");
            } else if (ethCall.getValue() == null) {
                result = "0";
                return result;
            } else {
                List<Type> someTypes = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                Log.i("토큰 : ", ethCall.getResult());
                for (int i = 0; i < someTypes.size(); i++) {
                    System.out.println("" + someTypes.get(i).getValue());
                    result = parseDecimal(someTypes.get(i).getValue().toString());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
