package com.universeluvv.ktnuvoting;

import android.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;


public class connectToEth {

    public Web3j ConnectToTestNet() {
        Web3j web3;

        web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/f78a47b36feb4649b1afd7a48a6fa47e"));

        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if (!clientVersion.hasError()) {
                Log.i("접속 검사"," 접속 성공");//Connected

            } else {
                Log.i("접속 검사"," 접속 실패");//Show Error
            }
        } catch (Exception e) {
            //Show Error
        }
        return web3;
    }
}
