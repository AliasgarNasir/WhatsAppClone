package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignUpUserName,edtSignUpEmail,edtSignUpPassword;
    private Button btnSignUp,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SignUp!");

        edtSignUpUserName = findViewById(R.id.edtSignUpUserName);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            transitionToWhatsApp();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                if (edtSignUpUserName.getText().toString().equals("") ||
                        edtSignUpEmail.getText().toString().equals("") ||
                        edtSignUpPassword.getText().toString().equals("")){
                    FancyToast.makeText(SignUp.this,"UserName, Email and Password are required.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

                }else{
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtSignUpUserName.getText().toString());
                    appUser.setEmail(edtSignUpEmail.getText().toString());
                    appUser.setPassword(edtSignUpPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up...");
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                FancyToast.makeText(SignUp.this,"SignUp Successful.",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                transitionToWhatsApp();
                            }else{
                                FancyToast.makeText(SignUp.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnLogin:
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void transitionToWhatsApp(){
        Intent intent = new Intent(SignUp.this,WhatsApp.class);
        startActivity(intent);
        finish();
    }
}
