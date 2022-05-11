package com.example.meloady;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    //COMMENT BRYAN
    private Button btnLogin, btnSignUp;
    private EditText userText, passwordText;
    private String user;
    private String password;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user2;

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        userText = view.findViewById(R.id.User2);
        passwordText = view.findViewById(R.id.Password2);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {
            user = userText.getText().toString().trim();
            password = passwordText.getText().toString().trim();
            if (user.equals("") || password.equals("")) {
                Toast.makeText(getContext(), "Algun campo esta vacio", Toast.LENGTH_SHORT).show();
            } else {
                logearUsuario(user,password);
            }

        });
        btnSignUp.setOnClickListener(v -> {
            replaceFragment();
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    roomsFragment();
                } else {

                    // El usuario es null
                }
            }
        };

        return view;
    }

    public void replaceFragment() {
        fragment = new SignUpFragment();
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void roomsFragment() {
        fragment = new RoomsFragment();
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    //Pruebas
//    public void uploadSongsFragment() {
//        fragment = new UploadSongFragment();
//        fragmentManager = getParentFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//    }





    private void logearUsuario(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Inicio fallido", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            roomsFragment();
//                            uploadSongsFragment();
                        }
                    }
                });


    }
}
