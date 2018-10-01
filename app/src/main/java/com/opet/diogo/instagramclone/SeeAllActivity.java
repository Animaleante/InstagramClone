package com.opet.diogo.instagramclone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opet on 24/09/2018.
 */

public class SeeAllActivity extends Activity {

    private ProgressBar progressLoad;
    private ListView listView;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        progressLoad = findViewById(R.id.progressLoad);
        listView = findViewById(R.id.list);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Query mQuery = mDatabaseRef.child("photos").orderByKey();
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Photo> photos = new ArrayList<Photo>();
                for(DataSnapshot livroSnapshot : dataSnapshot.getChildren()){
                    photos.add(livroSnapshot.getValue(Photo.class));
                    Log.d("DEBUG", livroSnapshot.getValue(Photo.class).getPhotoComment());
                }

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeeAllActivity.this, android.R.layout.simple_list_item_1, livrosNomes);
//                listLivros.setAdapter(adapter);
                SeeAllAdapter customAdapter = new SeeAllAdapter(getApplicationContext(), R.layout.single_list_item, photos);
                listView.setAdapter(customAdapter);

                progressLoad.setVisibility(ProgressBar.GONE);
                listView.setVisibility(ListView.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
