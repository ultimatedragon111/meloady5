package com.example.meloady;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class CreateRoomFragment extends Fragment implements IOnBackPressed{

    private TextInputLayout nameRoom,passwordRoom;
    private RadioGroup radioType;
    private RadioButton radioPrivate,radioPublic;
    private Button buttonAddRoom;
    FirebaseFirestore db;
    FirebaseUser user;
    Boolean isPublic;
    Room room;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_room, container, false);

        nameRoom = view.findViewById(R.id.nameRoom);
        passwordRoom = view.findViewById(R.id.passwordRoom);
        radioType = view.findViewById(R.id.radioType);
        radioPrivate = view.findViewById(R.id.TypePrivateBtn);
        radioPublic = view.findViewById(R.id.typePublicBtn);
        buttonAddRoom = view.findViewById(R.id.buttonCreate);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        radioPublic.setChecked(true);
        isPublic = true;

        radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioSelected = radioType.getCheckedRadioButtonId();
                switch (radioSelected) {
                    case R.id.TypePrivateBtn:
                        passwordRoom.setVisibility(View.VISIBLE);
                        isPublic = false;
                        break;
                    case R.id.typePublicBtn:
                        passwordRoom.setVisibility(View.GONE);
                        isPublic = true;
                        break;
                    default:
                        break;
                }
            }
        });
        buttonAddRoom.setOnClickListener(v ->{
            uploadRoom();
        });




        return view;
    }
    public void uploadRoom(){
        if (nameRoom.getEditText().getText().toString().trim().equals("")){
            Toast.makeText(getContext(),"Falta poner el nombre" , Toast.LENGTH_SHORT);
        }
        else if(passwordRoom.getEditText().getText().toString().trim().equals("") && !isPublic){
            Toast.makeText(getContext(),"Falta poner contraseña" , Toast.LENGTH_SHORT);
        }
        else if(isPublic){
            room = new Room(nameRoom.getEditText().getText().toString().trim(),isPublic);
            uploadFirebaseRoom(room);
        }
        else{
            room = new Room(nameRoom.getEditText().getText().toString().trim(),isPublic,passwordRoom.getEditText().getText().toString().trim());
            uploadFirebaseRoom(room);
        }

    }
    public void uploadFirebaseRoom(Room room){
        db.collection("rooms")
                .add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TTT funciona", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Room Uploaded to Database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TTT No funciona", "Error adding document", e);
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

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