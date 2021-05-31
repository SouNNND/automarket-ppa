package com.bar.automarket.mainfragment;

import android.content.Context;
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

import com.bar.automarket.R;
import com.bar.automarket.data.MyAdapter;
import com.bar.automarket.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FeedFragment extends Fragment {

    private static final String TAG = "GET_DATA";
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final Map<String, Post> posts = new HashMap<>();

    RecyclerView recyclerView;
    String make[] = {"Loading.."}, model[] = {"Loading.."};
    int images[];

    MyAdapter myAdapter;


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
        getData();

        //Storage for images
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //RecyclerView
        recyclerView = requireActivity().findViewById(R.id.recyclerView);

        myAdapter = new MyAdapter(requireActivity(), make, model, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity() ));

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            splitData();

            if(posts.isEmpty())
                Log.d(TAG, "EMPTY");
            else
                Log.d(TAG, "HAS VALUES");
            //Log.d(TAG, String.join(" ", make));
        }
    }

    public void getData() {
        mStore.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d((String) TAG, document.getId() + " => " + document.getData());
                            Post post = document.toObject(Post.class);
                            posts.put(document.getId(), post);
                        }
                    } else {
                        Log.d((String) TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void splitData() {
        ArrayList<String> makeAL = new ArrayList<>();
        ArrayList<String> modelAL = new ArrayList<>();
        for(Map.Entry<String, Post> p : posts.entrySet()) {
            makeAL.add(p.getValue().getMake());
            modelAL.add(p.getValue().getModel());
        }
        make = makeAL.toArray(new String[0]);
        model = modelAL.toArray(new String[0]);

        //Update recyclerView
        myAdapter = new MyAdapter(requireActivity(), make, model, images);
        recyclerView.setAdapter(myAdapter);
    }
}