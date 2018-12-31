package kh.edu.rupp.ckcc.derkamsan.PopularPlaces;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import kh.edu.rupp.ckcc.derkamsan.Homes.Home;
import kh.edu.rupp.ckcc.derkamsan.LibraryHelper;
import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;
import kh.edu.rupp.ckcc.derkamsan.Singleton;

public class PopularPlaceFragment extends Fragment implements OnRecyclerViewItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private PopularPlaceAdapter popularPlaceAdapter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Timer T;
    private int time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        T =new Timer();
        swipeRefreshLayout = view.findViewById(R.id.popular_place_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.MAGENTA, Color.GREEN);

        RecyclerView recyclerView = view.findViewById(R.id.popular_place_recyclerview_layout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        popularPlaceAdapter = new PopularPlaceAdapter();
        popularPlaceAdapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(popularPlaceAdapter);
        loadingProgress();
        if (Singleton.getInstance().getEvents() == null) {
            loadPopularPlacesFromFirestore();
        } else {
            popularPlaceAdapter.setPopularPlaces(Singleton.getInstance().getPopularPlaces());
            progressDialog.dismiss();
        }
    }

    private void loadPopularPlacesFromFirestore() {
        if (!LibraryHelper.isInternetAvailable(getActivity())) {
            LibraryHelper.showAlertDialog("No Internet","Internet is require. Please connect to internet.",getActivity());
            progressDialog.dismiss();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realtime query
        db.collection("PopularPlaces").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                PopularPlace[] popularPlaces = new PopularPlace[queryDocumentSnapshots.size()];
                int index = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PopularPlace popularPlace = documentSnapshot.toObject(PopularPlace.class);
                    popularPlace.setId(documentSnapshot.getId());
                    popularPlaces[index] = popularPlace;
                    index++;
                    Log.d("ckcc",popularPlace.getId());
                }
                popularPlaceAdapter.setPopularPlaces(popularPlaces);
                Singleton.getInstance().setPopularPlaces(popularPlaces);
                progressDialog.dismiss();
            }
        });

    }
    private void loadingProgress(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Data is loading...");
        progressDialog.show();
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        Intent intent = new Intent(getActivity(), PopularPlaceActivity.class);
        PopularPlace popularPlace = popularPlaceAdapter.getPopularPlacesToShow()[position];
        Gson gson = new Gson();
        String popularPlaceJson = gson.toJson(popularPlace);
        intent.putExtra("popularPlaceJson", popularPlaceJson);
        startActivity(intent);
    }

    @Override
    public void onRecyclerViewOptionItemClick(int position, View optionView) {

    }

    @Override
    public void onRefresh() {
        Singleton.getInstance().setPopularPlaces(null);

        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        },1000,1000);

        loadPopularPlacesFromFirestore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, time);
    }
}
