package com.example.meloady;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.transition.Hold;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    private Context context;
    private List<Song> songs;
    private String ROOM_ID;
    private String ROOM_NAME;

    public SongsAdapter(Context context, List<Song> songs, String roomID, String roomName) {
        this.context = context;
        this.songs = songs;
        this.ROOM_ID = roomID;
        this.ROOM_NAME= roomName;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.songs_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songName.setText(songs.get(position).getName());
        holder.artistName.setText(songs.get(position).getArtist());



        holder.songCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song songToAdd = new Song();
//                String user;
                songToAdd.setName(songs.get(holder.getAdapterPosition()).getName());
                songToAdd.setArtist(songs.get(holder.getAdapterPosition()).getArtist());
                songToAdd.setSongUrl(songs.get(holder.getAdapterPosition()).getSongUrl());
                songToAdd.setDuration(songs.get(holder.getAdapterPosition()).getDuration());

                //add to queue array of the room

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference queueAdd = db.collection("rooms").document(ROOM_ID);

// Atomically add a new region to the "regions" array field.
                queueAdd.update("songQu", FieldValue.arrayUnion(songToAdd));


//
//                db.collection("rooms").document(roomID)
//                        .set(songsQueue)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d("TAG", "Song added to room list!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("TAG", "Error writing document", e);
//                            }
//                        });

//                Fragment fragment = new RoomsFragment();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout, fragment);
//                fragmentTransaction.commit();

//                RoomFragment roomFragment = RoomFragment.newInstance();
////                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout, roomFragment);
//                fragmentTransaction.commit();
//
//                RoomFragment fragment2 = new RoomFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("ROOM_ID", ROOM_ID);
//                bundle.putString("ROOM_NAME",ROOM_NAME);
//                fragment2.setArguments(bundle);

                RoomFragment roomFragment = RoomFragment.newInstance(
                        ROOM_NAME,
                        ROOM_ID
                );
                //poner bundle para usar el mismo metodo para recoger el id???
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,roomFragment);
                fragmentTransaction.commit();

//                FragmentB fragmentB = new FragmentB ();
//                Bundle args = new Bundle();
//                args.putString(FragmentB.DATA_RECEIVE, data);
//                fragmentB .setArguments(args);

//                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction;
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout, fragment2);
//                fragmentTransaction.commit();
//                 RoomFragment roomFragment = new RoomFragment();
//
//
//                 roomFragment.onBackPressed();

//                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                Fragment2 fragment2 = new Fragment2();
//
//                Bundle bundle = new Bundle();
//                YourObj obj = SET_YOUR_OBJECT_HERE;
//                bundle.putSerializable("your_obj", obj);
//                fragment2.setArguments(bundle);
//                ft.replace(android.R.id.content, fragment2);
//                ft.addToBackStack(null);
//                ft.commit();
//
//                //pasar el id de room en la que estés para conseguir la lista de canciones de la room y añadirle a esta la cancion clickada
//                FirebaseFirestore.getInstance().collection("rooms").document(roomID).collection("songs").add(comment).addOnCompleteListener(task -> {
//                    binding.inputComment.getEditText().setText("");
//                });
//                commentsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songName;
        TextView artistName;
        CardView songCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            songCard = itemView.findViewById(R.id.songCard);
            artistName = itemView.findViewById(R.id.artistNameRow);
            songName = itemView.findViewById(R.id.songNameRow);
        }
    }
}
