package kh.edu.rupp.ckcc.derkamsan.Homes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private Home[] allHomes;
    private Home[] homesToShow;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_view_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {
        Home home = allHomes[i];
        homeViewHolder.txtTitle.setText("\t"+home.getTitle());
        homeViewHolder.imgHomeView.setImageURI(home.getImageUrl().get(0));
        homeViewHolder.txtDescription.setText("\t\t" + home.getDescription());
    }

    @Override
    public int getItemCount() {
        return homesToShow == null? 0 : homesToShow.length;
    }

    public Home[] getHomesToShow() {
        return homesToShow;
    }

    public void setHomes(Home[] homes) {
        allHomes = homes;
        homesToShow = homes;
        notifyDataSetChanged();
    }


    class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        SimpleDraweeView imgHomeView;
        TextView txtDescription;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_home_title_view_holder);
            imgHomeView = itemView.findViewById(R.id.img_home_view_holder);
            txtDescription = itemView.findViewById(R.id.txt_home_desc_view_holder);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onRecyclerViewItemClick(getAdapterPosition());
                }
            });
        }
    }
}
