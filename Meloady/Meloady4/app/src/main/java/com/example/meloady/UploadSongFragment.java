package com.example.meloady;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UploadSongFragment extends Fragment implements IOnBackPressed{


    private Button uploadButton;
    private ImageButton imageButton;
    private TextInputLayout nameSong, nameArtist;
    String uploadNameSong, uploadArtist, uploadUrl, uploadDuration, extension;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    Uri uri;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_song, container, false);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());

        uploadButton = view.findViewById(R.id.buttonUpload);
        imageButton = view.findViewById(R.id.SongUpload);
        nameSong = view.findViewById(R.id.NameSong);
        nameArtist = view.findViewById(R.id.NameArtist);
        user = FirebaseAuth.getInstance().getCurrentUser();



        imageButton.setOnClickListener(v -> {
            pickSong();
        });
        uploadButton.setOnClickListener(v -> {
            upload();
        });


        return view;
    }

    private void pickSong() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                uri = data.getData();
                uploadDuration = getSongDuration(uri);
                extension = getExtesion(uri);
            }
        }
    }

    public void upload() {
        if (uri == null) {
            Toast.makeText(getContext(), "Please select a song", Toast.LENGTH_SHORT).show();
        } else if (nameSong.getEditText().getText().toString().equals("")) {
            Toast.makeText(getContext(), "Song name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (nameArtist.getEditText().getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please add Artist, album name", Toast.LENGTH_SHORT).show();
        } else {
            uploadNameSong = nameSong.getEditText().getText().toString().trim() + "." + extension;
            uploadArtist = nameArtist.getEditText().getText().toString().trim();
            uploadFileToServer(uri, uploadNameSong, uploadArtist, uploadDuration);
        }
    }

    public void uploadFileToServer(Uri uri, String uploadNameSong, String uploadArtist, String duration) {
        StorageReference filePath = storageReference.child("Audios").child(uploadNameSong);
        progressDialog.show();
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlSong = uriTask.getResult();
                uploadUrl = urlSong.toString();
                uploadDetailsToDatabase(uploadNameSong, uploadUrl, uploadArtist, duration, user.getEmail());
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int currentProgress = (int) progress;
                progressDialog.setMessage("Uploading: " + currentProgress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Upload Failed! Please Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadDetailsToDatabase(String uploadNameSong, String uploadUrl, String uploadArtist, String songDuration, String user2) {

        Song song = new Song(uploadNameSong, uploadArtist, songDuration, uploadUrl, user2);
        Log.i("TTT user",user2);
        db.collection("songs")
                .add(song)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TTT funciona", "DocumentSnapshot added with ID: " + documentReference.getId());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Song Uploaded to Database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TTT No funciona", "Error adding document", e);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @SuppressLint("Range")
    public String getExtesion(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result.substring(result.lastIndexOf(".") + 1);
    }

    public String getSongDuration(Uri song) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(getContext(), song);
        String durationString = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long time = Long.parseLong(durationString);
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(time);
        int totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(time);
        int seconds = totalSeconds - (minutes * 60);
        if (String.valueOf(seconds).length() == 1) {
            return minutes + ":0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
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
