package com.universeluvv.ktnuvoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.AdapterPackage.VoteListviewAdapter;
import com.universeluvv.ktnuvoting.AdapterPackage.WalletMakeDialog;
import com.universeluvv.ktnuvoting.InfoClass.StudentInfo;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.InfoClass.VoteInfo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class VoteActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView username_tv, usernum_tv, usermajor_tv, usertoken_tv, userwallet_tv, drawer_logout_tv;
    Button wallet_make_btn;

    StudentInfo sinfo =new StudentInfo();
    ArrayList<Integer> checkingright =new ArrayList<Integer>();

    ListView progress_lv;
    TextView no_vote_tv;
    VoteListviewAdapter voteListviewAdapter;
    ArrayList<VoteInfo> voteInfos =new ArrayList<VoteInfo>();
    ArrayList<String>  vote_title_list = new ArrayList<String>();

    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        setupBouncyCastle();

        Intent intent =getIntent();
        sinfo.setId( intent.getStringExtra("userid"));
        sinfo.setMajor(intent.getStringExtra("major"));

        vote_title_list.clear();
        voteInfos.clear();

        //??????
        Toolbar mytoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mytoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_profile_24dp);


        //drawerlayout
        drawerLayout=findViewById(R.id.main_drawerlayout);
        drawerfunction();


        //?????????????????????
        progress_lv =findViewById(R.id.vote_lv);
        no_vote_tv =findViewById(R.id.progress_voting_textview);

        voteListviewAdapter =new VoteListviewAdapter(voteInfos);

        progress_lv.setAdapter(voteListviewAdapter);


//        progress_lv.setVisibility(View.VISIBLE);
//        no_vote_tv.setVisibility(View.GONE);

        progress_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(VoteActivity.this,VoteItemActivity.class);
                intent.putExtra("votename",voteInfos.get(position).getName());
                intent.putExtra("userid",sinfo.getId());
                intent.putExtra("major",sinfo.getMajor());
                startActivity(intent);




//                if(sinfo.getWalletaddress().equals("0")) Toast.makeText(VoteActivity.this, "????????? ????????????, ????????? ??????????????????.",Toast.LENGTH_SHORT).show();
//                else if(checkingright.get(position) ==1){
//                    Toast.makeText(VoteActivity.this, checkingright.get(position),Toast.LENGTH_SHORT).show();
//
//                }else Toast.makeText(VoteActivity.this, "?????? ????????? ?????? ???????????? ????????????.",Toast.LENGTH_SHORT).show();

            }
        });

        db = FirebaseDatabase.getInstance().getReference();
        db.child("vote").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    vote_title_list.clear();
                    voteInfos.clear();

                    no_vote_tv.setVisibility(View.INVISIBLE);
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String name = ds.getKey();
                        vote_title_list.add(name);
                    }

                    for(int i=0; i<vote_title_list.size(); i++){
                        VoteInfo vinfo =new VoteInfo();
                           vinfo.setName(dataSnapshot.child(vote_title_list.get(i)).child("name").getValue().toString());
                           vinfo.setTerm(dataSnapshot.child(vote_title_list.get(i)).child("term").getValue().toString());
                           vinfo.setCheck(dataSnapshot.child(vote_title_list.get(i)).child("check").getValue(Integer.TYPE));
                        vinfo.setCurrent_cnt(dataSnapshot.child(vote_title_list.get(i)).child("current_cnt").getValue(Integer.TYPE));
                        vinfo.setTotal_cnt(dataSnapshot.child(vote_title_list.get(i)).child("total_cnt").getValue(Integer.TYPE));
//                        checkingright.add(dataSnapshot.child(vote_title_list.get(i)).child("participant").child(sinfo.getMajor()).getValue(Integer.TYPE));



                           voteInfos.add(vinfo);
                           voteListviewAdapter.notifyDataSetChanged();
                    }
                }else{
                    no_vote_tv.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //????????????????????? ?????? ????????? ?????? ???????????????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_appbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : drawerLayout.openDrawer(Gravity.LEFT);
            case R.id.vote_add_btn:
                if (sinfo.getAuthority().equals("admin")) {
                    Intent intent = new Intent(VoteActivity.this, VoteAddActivity.class);
                    intent.putExtra("major", sinfo.getMajor());
                    intent.putExtra("userid", sinfo.getId());

                    startActivity(intent);
                } else Toast.makeText(VoteActivity.this, "????????? ????????? ??? ?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void drawerfunction(){

        username_tv =findViewById(R.id.username_drawer_tv);
        usermajor_tv =findViewById(R.id.usermajor_drawer_tv);
        usernum_tv = findViewById(R.id.usernumber_drawer_tv);
        usertoken_tv =findViewById(R.id.usertoken_cnt_drawer_tv);
        userwallet_tv = findViewById(R.id.userwalletaddress_drawer_tv);

        drawer_logout_tv =findViewById(R.id.logout_drawer_tv);
        wallet_make_btn= findViewById(R.id.make_walletaddress_drawer_btn);

        usernum_tv.setText(sinfo.getId());
        usermajor_tv.setText(sinfo.getMajor());

        DatabaseReference drawerdb = FirebaseDatabase.getInstance().getReference().child("student");
        CreateWallet createWallet = new CreateWallet();
        //String coin = createWallet.TokenCheck(sinfo.getWalletaddress());
        //Log.i("??????",coin);
        //sinfo.setCoin(coin);
        //drawerdb.child(sinfo.getMajor()).child(sinfo.getId()).child("coin").setValue(coin);
        drawerdb.child(sinfo.getMajor()).child(sinfo.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name =dataSnapshot.child("name").getValue().toString();
                String wallet = dataSnapshot.child("walletaddress").getValue().toString();
                sinfo.setAuthority(dataSnapshot.child("authority").getValue().toString());

                sinfo.setName(name);
                sinfo.setWalletaddress(wallet);

                //usertoken_tv.setText(coin);
                username_tv.setText(name);

                if(wallet.equals("0")){
                    wallet_make_btn.setVisibility(View.VISIBLE);
                    userwallet_tv.setText("????????? ????????? ????????????.");
                }else{
                    wallet_make_btn.setVisibility(View.GONE);
                    userwallet_tv.setText(wallet);
                    userwallet_tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        wallet_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               WalletMakeDialog wdialog =new WalletMakeDialog(VoteActivity.this, new WalletMakeDialog.WalletMakeDialogListener() {
                   @Override
                   public void Clickbtn(String pwd) {
                       CreateWallet createWallet = new CreateWallet();

                       createWallet.makeWallet(pwd);
                        //?????? ?????? ????????? ??????!

                      String toAddr = createWallet.ImportWallet(pwd);
                      DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("student").child(sinfo.getMajor()).child(sinfo.getId());
                      database.child("walletaddress").setValue(toAddr);
                      // toAddr = ????????? ?????? ?????? DB??? ?????? ????????? !
                      GetgasTask getgasTask = new GetgasTask(VoteActivity.this);
                      getgasTask.execute(toAddr);

                       Toast.makeText(VoteActivity.this, "????????? ?????????????????????.",Toast.LENGTH_SHORT).show();

                   }
               });
               wdialog.show();
            }
        });

        drawer_logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
}
