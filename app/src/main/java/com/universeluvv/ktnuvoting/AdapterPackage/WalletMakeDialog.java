package com.universeluvv.ktnuvoting.AdapterPackage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.universeluvv.ktnuvoting.R;
import com.universeluvv.ktnuvoting.VoteActivity;


public class WalletMakeDialog extends Dialog {
    EditText pwd_et;
    Button ok_btn;
    Context view;

    private  WalletMakeDialogListener walletMakeDialogListener;

    public WalletMakeDialog(@NonNull Context context, WalletMakeDialogListener walletMakeDialogListener) {
        super(context);
        this.view =context;
        this.walletMakeDialogListener =walletMakeDialogListener;
    }

    public interface  WalletMakeDialogListener{
        void Clickbtn(String pwd);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletaddress_pwd_dialog);

        pwd_et= findViewById(R.id.make_wallet_pwd_et);
        ok_btn =findViewById(R.id.make_wallet_ok_btn);


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwd_et.getText().toString();
                if(pwd.equals(null)){
                    Toast.makeText(getContext(),"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                }else{
                    walletMakeDialogListener.Clickbtn(pwd);
                    dismiss();
                }
            }
        });

    }
}
