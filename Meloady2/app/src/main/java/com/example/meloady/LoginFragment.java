package com.example.meloady;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private Button btnLogin;
    private EditText userText,passwordText;
    private String user,password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        btnLogin = view.findViewById(R.id.btnSignUp2);
        userText = view.findViewById(R.id.User2);
        passwordText = view.findViewById(R.id.Password2);

        btnLogin.setOnClickListener(v -> {
            user = userText.getText().toString().trim();
            password = passwordText.getText().toString().trim();
            FirebaseFirestore.getInstance().collection("users").add(new User(user, password));
        });

        return view;
    }
}