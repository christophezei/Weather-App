package com.example.christophe.weatherdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button continueBtn;
    EditText editTextUsername;
    SharedPreferences sharedPreferences;
    SharedPreferences nameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameSet=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String checkName=nameSet.getString("Value","0");
        if(checkName!="0"){

            Intent next=new Intent(MainActivity.this,weatherDemo.class);
            startActivity(next);

        }



        continueBtn=(Button) findViewById(R.id.btn_Continue);
        editTextUsername=(EditText) findViewById(R.id.editText_Name);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences=getSharedPreferences("SaveName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Value",editTextUsername.getText().toString());
                editor.apply();
                if(editTextUsername.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your name",
                            Toast.LENGTH_LONG).show();
                }else {

                    Intent myIntent = new Intent(MainActivity.this,
                            weatherDemo.class);
                    startActivity(myIntent);
                }

            }
        });


    }


}
