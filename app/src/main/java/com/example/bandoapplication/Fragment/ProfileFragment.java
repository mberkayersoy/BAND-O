package com.example.bandoapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bandoapplication.EditProfileActivity;
import com.example.bandoapplication.Model.Post;
import com.example.bandoapplication.Model.User;
import com.example.bandoapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileFragment extends Fragment {

    ImageView image_profile;
    TextView fullname, username, bio;
    Button edit_profile;
    String userID;

    FirebaseUser firebaseUser;
//    DatabaseReference reference;

    DocumentReference reference;

    String profileid;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");
        reference = FirebaseFirestore.getInstance().collection("Users").document(userID);

        image_profile = view.findViewById(R.id.image_profile);
        edit_profile = view.findViewById(R.id.edit_profile);
        fullname = view.findViewById(R.id.fullname);
        username = view.findViewById(R.id.username);
        bio = view.findViewById(R.id.bio);

        userInfo();

        if (profileid.equals(firebaseUser.getUid())) {
            edit_profile.setText("Edit Profile");
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Edit Profile")) {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        userInfo();
    }

    //    private void getNrPosts() {
//       // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int i=0;
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Post post = snapshot.getValue(Post.class);
//                    if (post.getPublisher().equals(profileid)) {
//                        i++;
//                    }
//                }
//                //posts.setText("" + i);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void userInfo() {
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());
            }
        });
    }
}