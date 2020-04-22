package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUserName,edtLoginPassword;
    private Button btnUserLogin,btnUserSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login!");

        edtLoginUserName = findViewById(R.id.edtLoginUserName);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnUserLogin = findViewById(R.id.btnUserLogin);
        btnUserSignUp = findViewById(R.id.btnUserSignUp);

        btnUserLogin.setOnClickListener(this);
        btnUserSignUp.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            transitionToWhatsApp();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUserLogin:
                if (edtLoginUserName.getText().toString().equals("") ||
                        edtLoginPassword.getText().toString().equals("")){
                    FancyToast.makeText(Login.this,"UserName and Password is required.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();

                    ParseUser.logInInBackground(edtLoginUserName.getText().toString(), edtLoginPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null){
                                        FancyToast.makeText(Login.this,"Login Successful.",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                        transitionToWhatsApp();
                                    }else{
                                        FancyToast.makeText(Login.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.btnUserSignUp:
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public void transitionToWhatsApp(){
        Intent intent = new Intent(Login.this,WhatsApp.class);
        startActivity(intent);
        finish();
    }
}
