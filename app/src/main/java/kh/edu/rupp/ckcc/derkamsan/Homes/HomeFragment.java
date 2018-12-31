package kh.edu.rupp.ckcc.derkamsan.Homes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import kh.edu.rupp.ckcc.derkamsan.Events.Event;
import kh.edu.rupp.ckcc.derkamsan.Events.EventDetailActivity;
import kh.edu.rupp.ckcc.derkamsan.LibraryHelper;
import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;
import kh.edu.rupp.ckcc.derkamsan.Singleton;

public class HomeFragment extends Fragment implements OnRecyclerViewItemClickListener ,SwipeRefreshLayout.OnRefreshListener{
    private HomeAdapter homeAdapter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Timer T;
    private int time;

    public Boolean isHomeContainData() {
        return homeAdapter != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        T =new Timer();
        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.MAGENTA, Color.GREEN);

        RecyclerView recyclerView = view.findViewById(R.id.home_recyclerview_layout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter();
        homeAdapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(homeAdapter);
        loadingProgress();
        if (Singleton.getInstance().getEvents() == null) {
            loadHomesFromFirestore();
        } else {
            homeAdapter.setHomes(Singleton.getInstance().getHomes());
            progressDialog.dismiss();
        }
    }
    private void loadingProgress(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Data is loading...");
        progressDialog.show();
    }
    private void loadHomesFromFirestore() {
        if (!LibraryHelper.isInternetAvailable(getActivity())) {
            LibraryHelper.showAlertDialog("No Internet","Internet is require. Please connect to internet.",getActivity());
            progressDialog.dismiss();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Realtime query
        db.collection("Homes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Home[] homes = new Home[queryDocumentSnapshots.size()];
                int index = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Home home = documentSnapshot.toObject(Home.class);
                    home.setId(documentSnapshot.getId());
                    homes[index] = home;
                    index++;
                }
                homeAdapter.setHomes(homes);
                Singleton.getInstance().setHomes(homes);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
        Home home = homeAdapter.getHomesToShow()[position];
        Gson gson = new Gson();
        String homeJson = gson.toJson(home);
        intent.putExtra("homeJson", homeJson);
        startActivity(intent);
    }

    @Override
    public void onRecyclerViewOptionItemClick(int position, View optionView) {

    }

    @Override
    public void onRefresh() {
        Singleton.getInstance().setHomes(null);
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        },1000,1000);
        loadHomesFromFirestore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, time);
    }
}
