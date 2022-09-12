package com.universeluvv.ktnuvoting;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CreateWallet{

    public void makeWallet(String password){
        try{
            File path = new File(Environment.getExternalStorageDirectory().getPath() + "/LightWallet");
            if (!path.exists()) {
                path.mkdir();
            }
            Log.i("검사",password);
            String name = WalletUtils.generateLightNewWalletFile(password, new File(String.valueOf(path)));
            String walletPath = path + "/" + name;

            Log.i("검사",walletPath);

            //Credentials cs = WalletUtils.loadCredentials(password,walletPath);

            //  cs.getAddress();
            //  cs.getEcKeyPair().getPrivateKey().toString(16)
            //  cs.getEcKeyPair().getPublicKey().toString(16)



        }catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | IOException
                | CipherException e) {
            e.printStackTrace();
            Log.i("검사","실패");
        }
    }

    public String ImportWallet(String password){
        String toAddr = null;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LightWallet";
        File directory = new File(path);
        File[] WalletDir = directory.listFiles();

        List<String> getWalletname = new ArrayList<>();

        for (int i=0; i< WalletDir.length; i++) {
            getWalletname.add(WalletDir[i].getName());
        }
        String Walletpath = path + "/" + getWalletname.get(0);
        Log.i("검사",Walletpath);
        try {
            Credentials cs = WalletUtils.loadCredentials(password, Walletpath);
            toAddr = cs.getAddress();
            Log.i("지갑주소",cs.getAddress());
        }
        catch (Exception e){
            Log.i("검사","지갑 로드 실패");
            return toAddr = "fail";
        }
        return toAddr;
    }

    public String GetprivateKey(String password){
        String privatekey = null;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LightWallet";
        File directory = new File(path);
        File[] WalletDir = directory.listFiles();

        List<String> getWalletname = new ArrayList<>();

        for (int i=0; i< WalletDir.length; i++) {
            getWalletname.add(WalletDir[i].getName());
        }
        String Walletpath = path + "/" + getWalletname.get(0);
        Log.i("검사",Walletpath);
        try {
            Credentials cs = WalletUtils.loadCredentials(password, Walletpath);
            privatekey = cs.getEcKeyPair().getPrivateKey().toString(16);
        }
        catch (Exception e){
            Log.i("검사","지갑 로드 실패");
        }
        return privatekey;
    }
}
