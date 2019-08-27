package com.tosh.poolandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.tosh.poolandroid.LoginRegistration.LoginActivity;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Retrofit.Model.User;
import com.tosh.poolandroid.Retrofit.NodeAuthService;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private Context context;
    private NodeAuthService api;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String email;

    MaterialToolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        email = pref.getString("email", "default");

        Retrofit retrofit = AuthRetrofitClient.getUser();
        api = retrofit.create(NodeAuthService.class);

        Call<List<User>> users = api.getUser(email);

        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users =  response.body();
                  String name = response.body().get(0).getUserName();
                  String phone = response.body().get(0).getUserPhone();
                  editor = pref.edit();
                  editor.putString("name", name);
                  editor.putString("phone", phone);
                  editor.commit();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USER",t.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.settings_appbar:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_appbar:
                logout();
                break;
            case R.id.cart_appbar:
                Toast.makeText(this, "Cart selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().remove("email").commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
