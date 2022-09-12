package com.universeluvv.ktnuvoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VoteAddActivity extends AppCompatActivity {

    String userid, major;
    int total_cnt ;

    TextView date_tv,time_tv , item_notice_tv;
    EditText title_et, item0_et,item1_et, item2_et,item3_et, item4_et;
    Button register_btn;

    ImageView date_btn, time_btn, item_plus_iv;
    CheckBox com, to, eco, elec, car, dri, invaildvote;

    long comc, carc, dric, elecc,ecoc,toc, tcnt;

    boolean comb=false, carb=false, drib=false, ecob=false, elecb=false, tob=false;

    ArrayList<String> itemname = new ArrayList<String>();
    ArrayList<String>  participant =new ArrayList<String>();

    DatabaseReference db, dbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_add);

        Intent intent =getIntent();
        userid=intent.getStringExtra("userid");
        major =intent.getStringExtra("major");

        Toolbar mytoolbar = findViewById(R.id.vote_add_toolbar);
        setSupportActionBar(mytoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        time_btn =findViewById(R.id.time_select_imgv);
        date_btn =findViewById(R.id.date_select_imgv);
        time_tv=findViewById(R.id.vote_add_time_tv);
        date_tv =findViewById(R.id.vote_add_date_tv);
        item_notice_tv=findViewById(R.id.voteitem_add_tv);
        title_et=findViewById(R.id.add_title_ev);
        register_btn=findViewById(R.id.voteadd_finish_btn);
        item_plus_iv =findViewById(R.id.voteitem_add_imgv);
        item0_et=findViewById(R.id.item0_et);
        item1_et=findViewById(R.id.item1_et);
        item2_et=findViewById(R.id.item2_et);
        item3_et=findViewById(R.id.item3_et);
        item4_et=findViewById(R.id.item4_et);

        com =findViewById(R.id.com_checkbox);
        to =findViewById(R.id.to_checkbox);
        eco=findViewById(R.id.eco_checkbox);
        elec=findViewById(R.id.elec_checkbox);
        dri=findViewById(R.id.dri_checkbox);
        car=findViewById(R.id.car_checkbox);
        invaildvote=findViewById(R.id.invaildvote_checkbox);

        itemadd();
        datepick();

        totalcntcheck();
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_et.getText().toString();
                String term = date_tv.getText().toString().trim()+" "+time_tv.getText().toString().trim();
                if(com.isChecked())  comb =true;
                if(car.isChecked())  carb =true;
                if(dri.isChecked())  drib =true;
                if(eco.isChecked())  ecob =true;
                if(elec.isChecked())  elecb =true;
                if(to.isChecked())  tob =true;

                if(title_et.getText().toString().length()==0) Toast.makeText(VoteAddActivity.this,"제목을 입력하세요,",Toast.LENGTH_SHORT).show();
                else if(term.equals("마감 날짜-마감 시간")) Toast.makeText(VoteAddActivity.this,"마감기한을 골라주세요,",Toast.LENGTH_SHORT).show();
                else if (item0_et.getText().toString().isEmpty()) Toast.makeText(VoteAddActivity.this,"한개 이상의 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                else {

                    db= FirebaseDatabase.getInstance().getReference().child("vote");

                    VoteDetailInfo voteDetailInfo =new VoteDetailInfo();
                    voteDetailInfo.setCheck(1);
                    voteDetailInfo.setName(title);
                    voteDetailInfo.setOrganizer(userid);
                    voteDetailInfo.setTerm(term);
                    voteDetailInfo.setCurrent_cnt(0);

                    db.child(title).setValue(voteDetailInfo);

                    if(comb) {
                        db.child(title).child("participant").child("컴퓨터정보공학과").setValue(1);
                        tcnt+=comc;
                    }else  db.child(title).child("participant").child("컴퓨터정보공학과").setValue(0);
                    if(carb){
                        db.child(title).child("participant").child("철도차량공학과").setValue(1);
                        tcnt +=carc;
                    }else db.child(title).child("participant").child("철도차량공학과").setValue(0);
                    if(drib){
                        db.child(title).child("participant").child("철도운전공학과").setValue(1);
                        tcnt+=dric;
                    }else db.child(title).child("participant").child("철도운전공학과").setValue(0);
                    if(ecob){
                        db.child(title).child("participant").child("철도경영물류학과").setValue(1);
                        tcnt +=ecoc;
                    }else db.child(title).child("participant").child("철도경영물류학과").setValue(0);
                    if(elecb){
                        db.child(title).child("participant").child("철도전자전기공학과").setValue(1);
                        tcnt +=elecc;
                    }else db.child(title).child("participant").child("철도전자전기공학과").setValue(0);
                    if(tob){
                        db.child(title).child("participant").child("철도인프라공학과").setValue(1);
                        tcnt +=toc;
                    }else db.child(title).child("participant").child("철도인프라공학과").setValue(0);

                    db.child(title).child("total_cnt").setValue(tcnt);

                    db.child(title).child("item").child(item0_et.getText().toString()).setValue(0);
                    if(item1_et.getText().toString().length() != 0 ) db.child(title).child("item").child(item1_et.getText().toString()).setValue(0);
                    if(item2_et.getText().toString().length() != 0 ) db.child(title).child("item").child(item2_et.getText().toString()).setValue(0);
                    if(item3_et.getText().toString().length() != 0 ) db.child(title).child("item").child(item3_et.getText().toString()).setValue(0);
                    if(item4_et.getText().toString().length() != 0 ) db.child(title).child("item").child(item4_et.getText().toString()).setValue(0);

                    if(invaildvote.isChecked()) db.child(title).child("item").child("무효표").setValue(0);

                    Toast.makeText(VoteAddActivity.this, "투표가 등록되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    //뒤로가기를 위한 버튼이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //항목 추가 이벤트 함수
    public void itemadd(){
        item_plus_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(View.VISIBLE==item4_et.getVisibility()) item_notice_tv.setText("최대 추가 갯수를 초과했습니다.");
                else if(View.VISIBLE==item3_et.getVisibility()) item4_et.setVisibility(View.VISIBLE);
                else if(View.VISIBLE==item2_et.getVisibility())item3_et.setVisibility(View.VISIBLE);
                else if(View.VISIBLE==item1_et.getVisibility())item2_et.setVisibility(View.VISIBLE);
                else if(View.VISIBLE==item0_et.getVisibility())item1_et.setVisibility(View.VISIBLE);
                }
        });
    }

    //datepicker함수
    public void datepick(){

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment =new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"datepicker");
            }
        });
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment =new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"timepicker");
            }
        });
    }


    public void totalcntcheck(){
        dbs =FirebaseDatabase.getInstance().getReference().child("student");

        dbs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals("컴퓨터정보공학과")) comc = ds.getChildrenCount();
                    if(ds.getKey().equals("철도차량공학과")) carc = ds.getChildrenCount();
                    if(ds.getKey().equals("철도운전공학과")) dric = ds.getChildrenCount();
                    if(ds.getKey().equals("철도경영물류학과")) ecoc = ds.getChildrenCount();
                    if(ds.getKey().equals("철도전자전기공학과")) elecc = ds.getChildrenCount();
                    if(ds.getKey().equals("철도인프라공학과")) toc = ds.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
