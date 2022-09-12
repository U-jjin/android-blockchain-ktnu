package com.universeluvv.ktnuvoting;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {


    EditText id_ev, pwd_ev;
    Button login_btn;
    Spinner major_sp;

    String id, pwd, major;

    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id_ev =findViewById(R.id.userid_ev);
        pwd_ev =findViewById(R.id.userpwd_ev);
        login_btn=findViewById(R.id.login_btn);

        major_sp =findViewById(R.id.login_major_spinner);


        db =FirebaseDatabase.getInstance().getReference();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id =id_ev.getText().toString();
                pwd =pwd_ev.getText().toString();
                major =major_sp.getSelectedItem().toString();

                if(major_sp.getSelectedItemPosition() == 0){
                    Toast.makeText(MainActivity.this, "학과를 선택하여주세요.",Toast.LENGTH_SHORT).show();
                }else{ if(id ==null ||pwd ==null )   Toast.makeText(MainActivity.this, "학번 또는 비밀번호를 입력하여 주세요.",Toast.LENGTH_SHORT).show();
                    else{ db.child("student").child(major).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(id).exists()){
                            String temp = dataSnapshot.child(id).child("pwd").getValue().toString();
                                if(temp.equals(pwd)){
                                    Intent intent =new Intent(MainActivity.this, VoteActivity.class);
                                    intent.putExtra("major",major);
                                    intent.putExtra("userid",id);
                                    startActivity(intent);
                                }else Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        }else  Toast.makeText(MainActivity.this, "학과 또는 학번이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                    }
                }
            }
        });


    }



}
