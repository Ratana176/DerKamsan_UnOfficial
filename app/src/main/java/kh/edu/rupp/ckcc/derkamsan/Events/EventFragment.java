package kh.edu.rupp.ckcc.derkamsan.Events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
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

import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;
import kh.edu.rupp.ckcc.derkamsan.Singleton;

public class EventFragment extends Fragment implements OnRecyclerViewItemClickListener{
    private EventAdapter eventAdapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.event_recyclerview_layout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        eventAdapter = new EventAdapter();
        eventAdapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(eventAdapter);

        if (Singleton.getInstance().getEvents() == null) {
            loadingProgress();
            loadEventsFromFirestore();
        } else {
            eventAdapter.setEvents(Singleton.getInstance().getEvents());

        }
    }
    private void loadingProgress(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Data is loading...");
        progressDialog.show();
    }


    @Override
    public void onRecyclerViewItemClick(int position) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        Event event = eventAdapter.getEventsToShow()[position];
        Gson gson = new Gson();
        String eventJson = gson.toJson(event);
        intent.putExtra("eventJson", eventJson);
        startActivity(intent);
    }

    @Override
    public void onRecyclerViewOptionItemClick(int position, View optionView) {

    }
    private void loadEventsFromFirestore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Event[] events = new Event[queryDocumentSnapshots.size()];
                int index = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Event event = documentSnapshot.toObject(Event.class);
                    event.setId(documentSnapshot.getId());
                    events[index] = event;
                    index++;
                }
                eventAdapter.setEvents(events);
                Singleton.getInstance().setEvents(events);
                progressDialog.dismiss();
            }
        });

    }

    public boolean isHomeContainData() {
        return eventAdapter != null;
    }
}
