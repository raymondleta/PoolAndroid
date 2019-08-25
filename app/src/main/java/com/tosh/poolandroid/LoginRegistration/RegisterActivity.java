package com.tosh.poolandroid.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tosh.poolandroid.MainActivity;
import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.NodeAuth;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    MaterialButton buttonLogin;
    MaterialButton buttonRegister;
    TextInputEditText inputName, inputEmail, inputPassword, inputConfirmPassword;

    NodeAuth api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init api
        Retrofit retrofit = AuthRetrofitClient.getInstance();
        api = retrofit.create(NodeAuth.class);

        // views
        buttonLogin = findViewById(R.id.register_login_btn);
        buttonRegister = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.name_register);
        inputEmail = findViewById(R.id.email_register);
        inputPassword = findViewById(R.id.password_register);
        inputConfirmPassword = findViewById(R.id.confirm_password_register);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(inputName.getText().toString(),inputEmail.getText().toString(),
                        inputPassword.getText().toString(), inputConfirmPassword.getText().toString());
            }
        });
    }

    private void registerUser(String name, String email, String password, String confirmPassword) {
        if(TextUtils.isEmpty(name)){
            inputName.setError("Enter name");
            return;
        }
        if(TextUtils.isEmpty(email)){
            inputEmail.setError("Enter email");
            return;
        }
        if(TextUtils.isEmpty(password)){
            inputPassword.setError("Enter password");
            return;
        }
        if(TextUtils.isEmpty(confirmPassword)){
            inputConfirmPassword.setError("Confirm password");
            return;
        }
        if(!TextUtils.equals(password, confirmPassword)){
            inputConfirmPassword.setError("Passwords don't match");
            return;
        }

        if(password.length()< 6){
            inputPassword.setError("Password should be atleast 6 characters");
        }

        compositeDisposable.add(api.registerUser(name,email,password,confirmPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {

                        if(res.equals("successful")){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }
}