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
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment implements IOnBackPressed {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ROOM_NAME = "param1";
    private static final String ROOM_ID = "param2";
    //private Song CURRENT_SONG
    public List<Song> roomPlaylist = new ArrayList<>();

    public FirebaseFirestore db;

    // TODO: Rename and change types of parameters
    private String roomNameRecuperado;
    private String roomIdRecuperado;
    private Song songPlaying;

    private AddFragment addFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    BottomNavigationView myroomnav;
    private Song selectedSong;
    private RecyclerView queue;
    private RecyclerView usersInroom;
    List<Song> songsList = new ArrayList<>();

    private UploadSongFragment uploadSongFragment;


    public static RoomFragment newInstance() {
        // Required empty public constructor
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String idFirebase) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ROOM_NAME, param1);
        args.putString(ROOM_ID, idFirebase);
//        args.putString(ROOM_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    public static RoomFragment newInstance(String roomname,String username) {
//        GuideDetailFragment fragment = new GuideDetailFragment();
//        Bundle args = new Bundle();
//        args.putInt(ICONO,icono);
//        args.putString(NAME,nombre);
//        args.putString(CITY,city);
//        args.putString(PRICE,price);
//        args.putString(DESC,desc);
//        args.putSerializable(IMAGENES,imagenes);
//
//
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        TextView roomName = view.findViewById(R.id.roomNameDetail);
        TextView roomId = view.findViewById(R.id.roomIdDetail);
        JcPlayerView jcplayerView = (JcPlayerView) view.findViewById(R.id.jcplayer);
        queue = view.findViewById(R.id.songsQueue);
        addFragment = new AddFragment();
        uploadSongFragment = new UploadSongFragment();
        myroomnav = view.findViewById(R.id.navigation);

        ArrayList<JcAudio> jcAudios = new ArrayList<>();
//        jcAudios.add(JcAudio.createFromURL("url audio2", "https://firebasestorage.googleapis.com/v0/b/meloady-1dde4.appspot.com/o/Audios%2F123.mp3?alt=media&token=020a1325-f52c-4316-ace4-2057b3b10f83"));
        jcAudios.add(JcAudio.createFromURL("url audio3", "https://firebasestorage.googleapis.com/v0/b/meloady-1dde4.appspot.com/o/Audios%2Fmanolo.mp3?alt=media&token=0e55525a-db38-4d87-b808-c31640e63b75"));
//        jcAudios.add(JcAudio.createFromAssets("Asset audio", "audio.mp3"));
//        jcAudios.add(JcAudio.createFromRaw("Raw audio", R.raw.audio));
        db = FirebaseFirestore.getInstance();
        //Queue songs recycler

//        roomPlaylist.add()
//        db.collection("songs").document().get(Source.valueOf())


        //----------------------------------------------------------------------------------------------------------------------


        if (getArguments() != null) {
            roomNameRecuperado = getArguments().getString(ROOM_NAME);
            roomIdRecuperado = getArguments().getString(ROOM_ID);
//            Bundle bundle = getArguments();
//            if (bundle != null) {
//                if (bundle.containsKey("ROOM_ID")) {
//                    String id = bundle.getString("ROOM_ID");
//                    String name = bundle.getString("ROOM_NAME");
//                    Log.i("FragmentROOM Log", "asdf:" + String.valueOf(id));
//                    Log.i("FragmentROOM Log", "asdf:" + String.valueOf(name));
//                    replaceFragmentV2(id, name);
//                }
//            } else {
//                Log.i("FragmentROOM Log", "room id is null");
//
//            }
//            selectedSong = getArguments().getBundle("selectedsong");
//            db.collection("rooms").get(roomIdRecuperado).addOnSuccessListener(v -> {
//               List<Song> songsL= v.toObjects(Song.class);
//               QueueSongsAdapter queueSongsAdapter = new QueueSongsAdapter(this.getContext(),songsL);
//               queue.setAdapter(queueSongsAdapter);
//               queue.setLayoutManager(new LinearLayoutManager(this.getContext()));
//            });
//            roomIdRecuperado = getArguments().getString(ROOM_ID);
//            songPlaying = getArguments().getBundle()
            roomName.setText(roomNameRecuperado);
            roomId.setText(roomIdRecuperado);
        }

        DocumentReference queueAdd = db.collection("rooms").document(ROOM_ID);
        db.collection("rooms")
                .document(roomIdRecuperado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Room roomq = document.toObject(Room.class);
                            for(Song s : roomq.getSongQu()){
                                System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssss");
                                System.out.println(s.getName());
                                System.out.println(s.getSongUrl());
                                jcAudios.add(JcAudio.createFromURL(s.getName(),s.getSongUrl()));
                            }
                            songsList.addAll(roomq.getSongQu());

                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

        jcplayerView.initPlaylist(jcAudios, null);
        jcplayerView.initAnonPlaylist(jcAudios);
        jcplayerView.createNotification(); // default icon
        QueueSongsAdapter queueSongsAdapter = new QueueSongsAdapter( this.getContext(),songsList);
        queue.setAdapter(queueSongsAdapter);
        queue.setLayoutManager(new LinearLayoutManager(getContext()));

        //----------------------------------------------------------------------------------------------------------------------
        myroomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addSong:
                        replaceFragmentV2(roomIdRecuperado, roomNameRecuperado);
                        return true;
                    case R.id.nav_upload:
                        replaceFragment(uploadSongFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        //----------------------------------------------------------------------------------------------------------------------
        return view;
    }


    public void replaceFragmentV2(String id, String name) {
        AddFragment fragment2 = new AddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("idR", id);
        bundle.putSerializable("nameR", name);
        fragment2.setArguments(bundle);
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment2);
        fragmentTransaction.commit();
    }


    public void replaceFragment(Fragment fragment) {
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = new RoomsFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        return true;
    }


}