package com.universeluvv.ktnuvoting.VoteItemFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.AdapterPackage.WalletMakeDialog;
import com.universeluvv.ktnuvoting.BalanceOfTask;
import com.universeluvv.ktnuvoting.CreateWallet;
import com.universeluvv.ktnuvoting.GetTokenTask;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.KnutToken;
import com.universeluvv.ktnuvoting.R;
import com.universeluvv.ktnuvoting.connectToEth;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressVoteFragment extends Fragment {

    private    String userid, major, votename;

    TextView title, term, complete_tv;
    RadioButton radio[]=new RadioButton[6];
    Button sumbit , getToken;
    LinearLayout linearLayout;
    RadioGroup radioGroup;

    VoteDetailInfo vote;
    DatabaseReference ds;

    String candidate[] = new String[6]; // 투표 후보 주소
    connectToEth connect = new connectToEth();
    KnutToken knutToken = new KnutToken();
    GetTokenTask getTokenTask = new GetTokenTask();
    ProgressDialog dialog , progressDialog;
    public ProgressVoteFragment(String userid, String major, String votename) {
       this.userid =userid;
       this.major =major;
       this.votename =votename;
       vote =new VoteDetailInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_progress_vote, container, false);
            Context context =container.getContext();

            title =view.findViewById(R.id.voteitem_progress_title_tv);
            term =view.findViewById(R.id.voteitem_progress_term_tv);
            complete_tv =view.findViewById(R.id.voteitem_complete_tv);
            radio[0]= view.findViewById(R.id.vote_radio0);
        radio[1]= view.findViewById(R.id.vote_radio1);
        radio[2]= view.findViewById(R.id.vote_radio2);
        radio[3]= view.findViewById(R.id.vote_radio3);
        radio[4]= view.findViewById(R.id.vote_radio4);
        radio[5] =view.findViewById(R.id.invaildvote_radio);
              sumbit =view.findViewById(R.id.voteitem_submit_btn);
            linearLayout =view.findViewById(R.id.itemshow_linear);
            radioGroup =view.findViewById(R.id.vote_ratiogroup);

         // 투표 후보 주소

        candidate[0] = "0x9BC9dA1Bbde104b38d902EA17BC087fe5C293A92";
        candidate[1] = "0xb08D52DceBf299A4F1330947612109C4A89097dE";
        candidate[2] = "0x73406dd65E679B979177CfD033deDC3312553527";
        candidate[3] = "0x4F13B97ff4e997Dd01589B8d305a8D66EBa584DA";
        candidate[4] = "0x0862c7722Babb384AC8B4D82d1d279f77Ce2309f";
        candidate[5] = "0x603Ca6aCccd810f89d5a88Ad3dF251e6C92173fD";
        getToken = view.findViewById(R.id.get_token_btn);



            completecheck();

           ds =FirebaseDatabase.getInstance().getReference();
            sumbit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalletMakeDialog wdialog = new WalletMakeDialog(context, new WalletMakeDialog.WalletMakeDialogListener() {
                        @Override
                        public void Clickbtn(String pwd) {
                            CreateWallet createWallet = new CreateWallet();
                            String toAddr = createWallet.ImportWallet(pwd);
                            if (toAddr.equals("fail")){
                                Toast.makeText(getActivity(), "비밀번호가 틀렸습니다. 다시 시도해 주세요.",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                if(knutToken.balanceOf(toAddr).equals("0"))
                                    Toast.makeText(getActivity(), "투표권이 없습니다.",Toast.LENGTH_SHORT).show();
                                else{
                                    String privateKey = createWallet.GetprivateKey(pwd);
                                    ArrayList<String> name = vote.getItem();
                                    ArrayList<Integer> cnt = vote.getItem_cnt();
                                    //다이얼로그 띄워주기
                                    for(int i=0; i<6; i++){
                                        if(radio[i].isChecked()) {
                                            try {
                                                knutToken.execute(privateKey,candidate[i]);
                                            }catch (Exception e){

                                            }
                                            //Toast.makeText(getActivity(),"투표를 완료하였습니다.",Toast.LENGTH_SHORT).show();
                                            //ds.child("vote").child(votename).child("item").child(name.get(i)).setValue(cnt.get(i)+1);
                                            //ds.child("vote").child(votename).child("current_cnt").setValue(vote.getCurrent_cnt()+1);
                                            //ds.child("student").child(major).child(userid).child("complete").child(votename).setValue(1);
                                            Toast.makeText(getActivity(),"투표를 완료하였습니다.",Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        if(i==5) Toast.makeText(getActivity(),"한가지 항목을 선택하여 주세요,",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                        }
                    });
                    wdialog.show();
                    /*
                    ArrayList<String> name = vote.getItem();
                    ArrayList<Integer> cnt = vote.getItem_cnt();
                        //다이얼로그 띄워주기
                    for(int i=0; i<6; i++){
                        if(radio[i].isChecked()) {

                            Toast.makeText(getActivity(),"투표를 완료하였습니다.",Toast.LENGTH_SHORT).show();
                            //ds.child("vote").child(votename).child("item").child(name.get(i)).setValue(cnt.get(i)+1);
                            //ds.child("vote").child(votename).child("current_cnt").setValue(vote.getCurrent_cnt()+1);
                            ds.child("student").child(major).child(userid).child("complete").child(votename).setValue(1);
                            break;
                        }
                        if(i==5) Toast.makeText(getActivity(),"한가지 항목을 선택하여 주세요,",Toast.LENGTH_SHORT).show();
                    }
                    */

                }   //onClick
            });

            // 투표권 얻기 버튼
        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletMakeDialog wdialog =new WalletMakeDialog(context, new WalletMakeDialog.WalletMakeDialogListener() {
                    @Override
                    public void Clickbtn(String pwd) {
                        CreateWallet createWallet = new CreateWallet();
                        String toAddr = createWallet.ImportWallet(pwd);
                        if (toAddr.equals("fail")){
                            Toast.makeText(getActivity(), "비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dialog = new ProgressDialog(getActivity());
                            dialog.setTitle("투표토큰을 얻는 중입니다.");
                            dialog.setMessage("투표를 위해 필요한 KnutToken을 요청하고 있습니다.");
                            dialog.setIndeterminate(true);
                            dialog.setCancelable(true);
                            dialog.show();
                            try {
                                String result = getTokenTask.execute(toAddr).get();
                                if(result.equals("success"))
                                    Toast.makeText(getActivity(), "투표권을 얻었습니다.",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                            }catch (Exception e){
                                Toast.makeText(getActivity(), "오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                wdialog.show();
            } // onClick
        });
        return view;
    }


    public void completecheck(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot sds =dataSnapshot.child("student").child(major).child(userid);
                DataSnapshot vds =dataSnapshot.child("vote").child(votename);

                title.setText(votename);
                term.setText(vds.child("term").getValue().toString());
                vote.setOrganizer(vds.child("organizer").getValue().toString());
                vote.setTotal_cnt(vds.child("total_cnt").getValue(Integer.TYPE));
                vote.setCurrent_cnt(vds.child("current_cnt").getValue(Integer.TYPE));


                if( sds.child("complete").child(votename).exists()) {
                    linearLayout.setVisibility(View.GONE);
                    complete_tv.setVisibility(View.VISIBLE);
                    sumbit.setVisibility(View.GONE);
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                    complete_tv.setVisibility(View.GONE);
                    sumbit.setVisibility(View.VISIBLE);

                    ArrayList<String> itemname = new ArrayList<String>();
                    ArrayList<Integer> itemname_cnt = new ArrayList<Integer>();

                    for(DataSnapshot ds : vds.child("item").getChildren()){
                        itemname.add(ds.getKey());
                        itemname_cnt.add(ds.getValue(Integer.TYPE)); }

                    vote.setItem(itemname);
                    vote.setItem_cnt(itemname_cnt);

                    radiocheck(vote.getItem(), vote.getItem_cnt());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void radiocheck(ArrayList<String> itemname,  ArrayList<Integer> itemname_cnt){
        int a=0; boolean t =false;

        for(int i=0; i<itemname.size(); i++ ){
            radio[i].setVisibility(View.VISIBLE);
            if(itemname.get(i).equals("무효표")){
                t=true;
                a=i;
            }
        }
        if(t){
         itemname.remove(a);
         itemname.add("무효표");
         int temp =itemname_cnt.get(a);
         itemname_cnt.remove(a);
         itemname_cnt.add(temp);
        }
        vote.setItem(itemname);
        vote.setItem_cnt(itemname_cnt);

        for(int i=0; i<itemname.size(); i++){
            radio[i].setText(itemname.get(i));
        }
    }

    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("잠시만 기다려 주세요");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }

}
