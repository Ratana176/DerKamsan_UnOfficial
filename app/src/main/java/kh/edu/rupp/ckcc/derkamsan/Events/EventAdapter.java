package kh.edu.rupp.ckcc.derkamsan.Events;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private Event[] allEvents;
    private Event[] eventsToShow;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setEvents(Event[] events) {
        allEvents = events;
        eventsToShow = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_event_view_holder,viewGroup,false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        Event event = eventsToShow[i];
        eventViewHolder.imgPlace.setImageURI(event.getImageUrl().get(0));
        eventViewHolder.txtTitle.setText(event.getTitle());
        eventViewHolder.txtDate.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return eventsToShow == null? 0 : eventsToShow.length;
    }

    public Event[] getEventsToShow() {
        return eventsToShow;
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView imgPlace;
        TextView txtTitle;
        TextView txtDate;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.img_event_view_holder);
            txtTitle = itemView.findViewById(R.id.txt_event_title);
            txtDate = itemView.findViewById(R.id.txt_event_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onRecyclerViewItemClick(getAdapterPosition());
                }
            });
        }
    }
}
