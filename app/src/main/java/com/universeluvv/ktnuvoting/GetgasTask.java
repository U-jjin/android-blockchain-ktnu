package com.universeluvv.ktnuvoting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;




import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;


public class GetgasTask extends AsyncTask<String,String,Void> {
    private Context view;
    ProgressDialog dialog;
    connectToEth connect = new connectToEth();

    public GetgasTask(Context view){
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(view);
        dialog.setTitle("지갑을 생성하고 있습니다.");
        dialog.setMessage("잠시만 기다려주세요");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        Web3j web3 = connect.ConnectToTestNet();
        String privateKey = "8BEBA8BBBC0E8D1C3E06C3939F9E83B7D8F2E806FB23387D4BF76C68901BF48F";
        Credentials credentials = Credentials.create(privateKey);
        //dialog.setMessage("투표권을 위한 GAS를 얻는 중입니다.");
        Log.i("검사","t1");

        try{
            BigInteger amountWei = Convert.toWei("0.131313", Convert.Unit.ETHER).toBigInteger();
            TransactionReceipt receipt = Transfer.sendFunds(web3,credentials,strings[0],new BigDecimal("0.002"),Convert.Unit.ETHER).sendAsync().get();
            Log.i("검사","t2");
            //Toast.makeText(this, "Transaction complete: " +receipt.getTransactionHash(), Toast.LENGTH_LONG).show();
            Log.i("TXHASH",receipt.getTransactionHash());
        }
        catch (Exception e){
            Log.i("검사","t3");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        super.onPostExecute(aVoid);
    }
}
