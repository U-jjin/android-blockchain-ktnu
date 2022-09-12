package com.universeluvv.ktnuvoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.InfoClass.StudentInfo;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;
import com.universeluvv.ktnuvoting.VoteItemFragment.EndVoteFragment;
import com.universeluvv.ktnuvoting.VoteItemFragment.ProgressVoteFragment;

import java.util.ArrayList;

public class VoteItemActivity extends AppCompatActivity {

    String  votename;
    StudentInfo info =new StudentInfo();

    VoteDetailInfo voteinfo =new VoteDetailInfo();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    DatabaseReference ds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_item);

        Intent intent =getIntent();
        info.setId(intent.getStringExtra("userid"));
        info.setMajor(intent.getStringExtra("major"));
        votename =intent.getStringExtra("votename");


        ds=FirebaseDatabase.getInstance().getReference();
        votedataset();

        Toolbar mytoolbar = findViewById(R.id.vote_item_toolbar);
        setSupportActionBar(mytoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
    }


    //드로우레이아웃 회원 정보를 위한 버튼이벤트

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.voteitem_appbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : finish();
            case R.id.vote_delete_btn :



        }
        return super.onOptionsItemSelected(item);
    }

    public void votedataset(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =fragmentManager.beginTransaction();

        DatabaseReference drawerdb = FirebaseDatabase.getInstance().getReference();
        drawerdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot votedata =dataSnapshot.child("vote");

                if(votedata.child(votename).child("check").getValue(Integer.TYPE).equals(1)) fragmentTransaction.replace(R.id.vote_item_fragment,new ProgressVoteFragment(info.getId(),info.getMajor(),votename)).commit();
                else if(votedata.child(votename).child("check").getValue(Integer.TYPE).equals(2)) fragmentTransaction.replace(R.id.vote_item_fragment,new EndVoteFragment(info.getId(),info.getMajor(),votename)).commit();

               voteinfo.setOrganizer(votedata.child(votename).child("organizer").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void deletevote(){

    }
}

