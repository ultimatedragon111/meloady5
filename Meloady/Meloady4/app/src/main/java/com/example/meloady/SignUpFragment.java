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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpFragment extends Fragment {

    private Button btnLogin, btnSignUp;
    private EditText user2, password2, email2;
    private String user, password, email;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private boolean emailIsIn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        btnLogin = view.findViewById(R.id.btnLogin2);
        btnSignUp = view.findViewById(R.id.btnSignUp2);
        user2 = view.findViewById(R.id.User2);
        password2 = view.findViewById(R.id.Password2);
        email2 = view.findViewById(R.id.Email2);
        mAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(v -> {
            emailIsIn = false;
            user = user2.getText().toString().trim();
            password = password2.getText().toString().trim();
            email = email2.getText().toString().trim();
            if (user.equals("") || password.equals("") || email.equals("")) {
                Toast.makeText(getContext(), "Algun campo esta vacio", Toast.LENGTH_SHORT).show();
            } else {
                crearUsuario(email,password,user);
            }

        });
        btnLogin.setOnClickListener(v -> {
            replaceFragment();
        });
        return view;
    }

    public void roomsFragment() {
        fragment = new RoomsFragment();
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment() {
        fragment = new LoginFragment();
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void crearUsuario(String email,String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Creacion de usuario fallido", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                roomsFragment();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

}
