package com.pau.enrech.sharedcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class CounterActivity extends AppCompatActivity {

    private TextView counterView;
    private FirebaseDatabase db;
    private DatabaseReference counter;
    private ValueEventListener counterListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        counterView = findViewById(R.id.counter);
        
        db= FirebaseDatabase.getInstance();
        counter = db.getReference("counter");

    }

    @Override
    protected void onStart() {
        super.onStart();
        counterListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long val = dataSnapshot.getValue(Long.class);
                counterView.setText(String.valueOf(val));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Log.e("counter","No se ha podido leer el contador");
            }
        };
        counter.addValueEventListener(counterListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        counter.removeEventListener(counterListener);
    }

    public void sumarUn(View view){
        counter.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long val = mutableData.getValue(Long.class);
                if (val !=null ){
                    mutableData.setValue(val +1);
                    return Transaction.success(mutableData);
                }
                return null;
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.i("counter","onComplete");
            }
        });
    }

    public void restarUn(View view){
        counter.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long val = mutableData.getValue(Long.class);
                if (val !=null ){
                    mutableData.setValue(val -1);
                    return Transaction.success(mutableData);
                }
                return null;
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.i("counter","onComplete");
            }
        });
    }
    public void reset(View view){
        counter.setValue(0);
    }
}
