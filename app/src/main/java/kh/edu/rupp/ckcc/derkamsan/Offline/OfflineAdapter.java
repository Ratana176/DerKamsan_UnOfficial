package kh.edu.rupp.ckcc.derkamsan.Offline;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.offlineViewHolder>{
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    private Offline[] data;
    public void setData(Offline[] data) {
        this.data = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public offlineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View offlineViewLayout = inflater.inflate(R.layout.activity_offline_items,viewGroup,false);
        offlineViewHolder offlineViewHolder = new offlineViewHolder(offlineViewLayout);
        return offlineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull offlineViewHolder offlineViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return data==null? 5: data.length;
    }

    class offlineViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPlace;
        private TextView txtPlace;
        private TextView txtSizeOfData;
        public offlineViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace =  itemView.findViewById(R.id.img_Place);
            txtPlace = itemView.findViewById(R.id.offline_text_place);
            txtSizeOfData =itemView.findViewById(R.id.offline_text_data);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onRecyclerViewItemClick(getAdapterPosition());
                }
            });
        }
    }
}
