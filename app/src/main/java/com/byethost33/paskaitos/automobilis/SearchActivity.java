package com.byethost33.paskaitos.automobilis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button newEntry = (Button) findViewById(R.id.newEntry);

        newEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent goToNewEntryActivity = new Intent(SearchActivity.this, NewEntry.class);
                startActivity(goToNewEntryActivity);
            }
        });

    }
}
