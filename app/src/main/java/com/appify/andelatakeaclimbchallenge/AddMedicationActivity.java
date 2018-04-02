package com.appify.andelatakeaclimbchallenge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMedicationActivity extends AppCompatActivity {
    private EditText nameEt, intervalEt;
    private Button btnAdd;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        nameEt = (EditText)findViewById(R.id.nameEt);
        intervalEt = (EditText)findViewById(R.id.numberET);
        btnAdd = (Button)findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(AddMedicationActivity.this);
                progressDialog.show();
                databaseReference = FirebaseDatabase.getInstance().getReference("Medication");
                if (nameEt.getText().length()!=0 && intervalEt.getText().length()!=0) {
                    String name = nameEt.getText().toString();
                    int interval = Integer.parseInt(intervalEt.getText().toString());
                    String id = databaseReference.push().getKey();
                    Medication med = new Medication(id, name, interval);
                    databaseReference.child(id).setValue(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(AddMedicationActivity.this,
                                    "Medication added successfully,",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AddMedicationActivity.this,
                            "One or more field have not been filled",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
