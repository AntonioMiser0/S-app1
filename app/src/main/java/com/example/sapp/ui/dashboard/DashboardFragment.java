package com.example.sapp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private ArrayAdapter arrayAdapter;
    List<Event> rowItems;
    FirebaseAuth mAuth;
    String currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View v=inflater.inflate(R.layout.fragment_dashboard,container,false);
        TextView textView = (TextView) v.findViewById(R.id.text_dashboard);
        rowItems = new ArrayList<Event>();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.details, rowItems );
        addEvent();
        SwipeFlingAdapterView flingContainer=(SwipeFlingAdapterView) v.findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
               Toast.makeText(getActivity(), "Left!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Right!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                // al.add("XML ".concat(String.valueOf(i)));
                //arrayAdapter.notifyDataSetChanged();
                //Log.d("LIST", "notified");
                //i++;

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    return v;
    }
    private void addEvent() {

        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference eventsDb= FirebaseDatabase.getInstance().getReference().child("Users");
        eventsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                boolean lazes=(snapshot.getKey().toString()).equals(currentUser);
                if(snapshot.child("dogadaj").getValue().equals(true)&&(!lazes)){
                    Event Item = new Event(snapshot.getKey(), snapshot.child("Event").child("description").getValue().toString(),
                            snapshot.child("Event").child("location").getValue().toString(),
                            snapshot.child("Event").child("category").getValue().toString()
                            , snapshot.child("Event").child("date").getValue().toString()
                            , snapshot.child("Event").child("pictureUrl").getValue().toString()
                            , snapshot.child("fullName").getValue().toString(), snapshot.child("age").getValue().toString());
                    rowItems.add(Item);
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), snapshot.getKey().toString(),Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


}