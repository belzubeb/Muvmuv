package com.example.muvmuv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    private EditText editUser, editEmail, editPassword, editPasswordConf;
    private Button btnlogin, btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private UserProfileChangeRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUser = findViewById(R.id.valueuser);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editPasswordConf = findViewById(R.id.Valuepasswordcheck);
        btnlogin = findViewById(R.id.buttonlogin);
        btnRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("sabar bos");
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(v -> {
            finish();
        });
        btnRegister.setOnClickListener(v -> {
            if (editUser.getText().length()>0 && editEmail.getText().length()>0 && editPassword.getText().length()>8 && editPasswordConf.getText().length()>8){
                if(editPassword.getText().toString().equals(editPasswordConf.getText().toString())){
                    register(editUser.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Silahkan masukan password yang sama!!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi dengan benar semua data!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void register(String username, String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()&& task.getResult()!=null) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            reload();
                        }
                    });
                }else{
                        Toast.makeText(getApplicationContext(), "Register gagal bos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}