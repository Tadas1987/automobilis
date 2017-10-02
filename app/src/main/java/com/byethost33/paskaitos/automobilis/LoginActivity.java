package com.byethost33.paskaitos.automobilis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

        private Button submit, regist;
        private EditText username, password;

        private CheckBox rememberMeCheckBox;

        private static final String REGISTER_URL = "http://paskaitos.byethost33.com/demo/mobile/LoginConfirmation.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        RegisterButton();
        Authentication();
    }

    public void RegisterButton() {
        regist = (Button) findViewById(R.id.regist);

        regist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent goToRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goToRegisterActivity);
            }
        });
    }

public void Authentication() {

    submit = (Button)findViewById(R.id.submit);
    username = (EditText)findViewById(R.id.username);
    password = (EditText)findViewById(R.id.password);
    rememberMeCheckBox = (CheckBox) findViewById(R.id.remember);

    final VartotojoRegistracija dude = new VartotojoRegistracija(getApplicationContext());

    rememberMeCheckBox.setChecked(dude.isRemembered());

    if (dude.isRemembered()) {
        username.setText(dude.getUsernameForLogin(), TextView.BufferType.EDITABLE);
        password.setText(dude.getPasswordForLogin(), TextView.BufferType.EDITABLE);
    }else {
        username.setText("", TextView.BufferType.EDITABLE);
        password.setText("", TextView.BufferType.EDITABLE);
    }


    username.setError(null);
    password.setError(null);

    submit.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            String username = LoginActivity.this.username.getText().toString();
            String password = LoginActivity.this.password.getText().toString();

               /* Toast.makeText(getApplicationContext(),
                        "username:"+username+"\n"+
                        "password:"+password, Toast.LENGTH_SHORT).show();*/

            boolean cancel = false;
            View focusView = null;

            if (!IsValid(username)) {
                LoginActivity.this.username.setError(getString(R.string.login_invalid_username));
                focusView = LoginActivity.this.username;
                cancel = true;
            }

            if (!IsValid(password)) {
                LoginActivity.this.password.setError(getString(R.string.login_invalid_password));
                focusView = LoginActivity.this.password;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {

                if (rememberMeCheckBox.isChecked()){
                    dude.setUsernameLogin(username);
                    dude.setPasswordForLogin(password);
                    dude.setRemembered(true);
                }else {
                    dude.setUsernameLogin(username);
                    dude.setPasswordForLogin(password);
                    dude.setRemembered(false);
                }
                postToDatabase(dude);
            }
        }
    });




    }

    private boolean IsValid(String credentials) {
        final String CREDENTIALS_PATTERN = "^([0-9a-zA-Z]{3,15})+$";
        Pattern pattern = Pattern.compile(CREDENTIALS_PATTERN);

        Matcher matcher = pattern.matcher (credentials);
        return matcher.matches();
    }

    private void postToDatabase (final VartotojoRegistracija dude){
        class NewEntry extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            DB database = new DB();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Prašome palaukti",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("202")){
                    Intent myIntent = new Intent(LoginActivity.this, SearchActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    LoginActivity.this.startActivity(myIntent);
                    Toast.makeText(getApplicationContext(),"paejo " + s,Toast.LENGTH_LONG).show();

                }else if (s.equals("203")){
                    Toast.makeText(getApplicationContext(),"Tokio vartotojo vardo su slaptažodžiu nėra",Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                // Pirmas string raktas, antras string reiksme
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);



                String result = database.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }
        NewEntry ru = new NewEntry();
        ru.execute(dude.getUsernameForLogin(), dude.getPasswordForLogin());
    }
}