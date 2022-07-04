package com.example.topandroidrepositories;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView androidProjectListView;
    //Demo data to populate into Listview
    String projectList[] = new String[] {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5",
            "Project 6", "Project 7", "Project 8", "Project 9", "Project 10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidProjectListView = findViewById(R.id.projectListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, R.id.textView, projectList);

        androidProjectListView.setAdapter(arrayAdapter);
    }
}