package com.example.meloady;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomsFragment#} factory method to
 * create an instance of this fragment.
 */
public class RoomsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView title;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    List<Room> rooms = new ArrayList<>();
    private FrameLayout roomsFrame;
    BottomNavigationView roomsNav;

    //private RoomFragment roomFragment;
    private UploadSongFragment uploadSongFragment;
    private CreateRoomFragment createRoomFragment;
    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        title = view.findViewById(R.id.RoomsTitle);
        recyclerView = view.findViewById(R.id.RoomsRecycler);
        roomsFrame = (FrameLayout) view.findViewById(R.id.rooms_frame);
        roomsNav = (BottomNavigationView) view.findViewById(R.id.navigation);

        //roomFragment = new RoomFragment();
        uploadSongFragment = new UploadSongFragment();

        createRoomFragment = new CreateRoomFragment();

        db = FirebaseFirestore.getInstance();
        db.collection("rooms")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Room> roomRetrieve = new ArrayList<>();
                    for (QueryDocumentSnapshot q : task.getResult()){
                        Room tmproom = new Room();
                        tmproom = q.toObject(Room.class);
                        tmproom.setId(q.getId());
                        roomRetrieve.add(tmproom);
                    }
                    RoomsAdapter roomsAdapter = new RoomsAdapter(roomRetrieve, getContext());
                    recyclerView.setAdapter(roomsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }
        });


        roomsNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addSong:
                        replaceFragment(createRoomFragment);
                        return true;
                    case R.id.nav_upload:
                        replaceFragment(uploadSongFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });


        return view;
    }

    public void replaceFragment(Fragment fragment) {

        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}