package com.bar.automarket.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.bar.automarket.AdInfoActivity;
import com.bar.automarket.R;
import com.bar.automarket.data.MyAdapter;
import com.bar.automarket.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeedFragment extends Fragment implements MyAdapter.OnAdListener {

    //debug
    protected static final String TAG = "GET_DATA";
    protected static final String REALTIME = "REALTIME_DATA";
    protected static final String TAG2 = "FIRST ACTIVITY";

    //Firebase
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage storage;
    StorageReference storageReference;

    //ui components
    protected RecyclerView recyclerView;
    Toolbar myToolbar;

    //vars
    protected MyAdapter myAdapter;
    protected final Map<String, Post> posts = new HashMap<>();
    protected final ArrayList<Post> mPosts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        //Storage for images
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //RecyclerView
        recyclerView = requireActivity().findViewById(R.id.recyclerView);

        myAdapter = new MyAdapter(requireActivity(), mPosts, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity() ));

        getData();
    }

    protected void getData() {
        //Listen if data change -> refresh list
        mStore.collection("posts")
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(REALTIME, "Listen failed.", e);
                        return;
                    }
                    posts.clear();
                    for (QueryDocumentSnapshot document : value) {
                        Post post = document.toObject(Post.class);
                        posts.put(document.getId(), post);
                    }

                    splitData();
                });
    }

    protected void splitData() {
        mPosts.clear();
        for(Map.Entry<String, Post> p : posts.entrySet()) {
            mPosts.add(p.getValue());
        }

        //Update recyclerView
        myAdapter = new MyAdapter(requireActivity(), mPosts, this);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onAdClick(int position) {
        Intent intent = new Intent(requireActivity(), AdInfoActivity.class);
        intent.putExtra("model", mPosts.get(position).getModel());
        intent.putExtra("make", mPosts.get(position).getMake());
        intent.putExtra("imgId", mPosts.get(position).getImgId());
        intent.putExtra("year", mPosts.get(position).getYear());
        intent.putExtra("mileage", mPosts.get(position).getMileage());
        intent.putExtra("fuel", mPosts.get(position).getFuel());
        intent.putExtra("displacement", mPosts.get(position).getDisplacement());
        intent.putExtra("power", mPosts.get(position).getPower());
        intent.putExtra("userId", mPosts.get(position).getUserId());
        intent.putExtra("price", mPosts.get(position).getPrice());

        Log.d(TAG2, mPosts.get(position).getModel() + ": " + mPosts.get(position).getMake());
        startActivity(intent);
    }
}