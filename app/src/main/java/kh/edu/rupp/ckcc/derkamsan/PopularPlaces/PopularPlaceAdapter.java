package kh.edu.rupp.ckcc.derkamsan.PopularPlaces;

import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;

public class PopularPlaceAdapter extends RecyclerView.Adapter<PopularPlaceAdapter.PopularPlaceViewHolder>{

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private PopularPlace[] allPopularPlaces;
    private PopularPlace[] popularPlacesToShow;
    @NonNull
    @Override
    public PopularPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PopularPlaceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_popular_place_view_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularPlaceViewHolder popularPlaceViewHolder, int i) {
        PopularPlace popularPlace = popularPlacesToShow[i];
        String place = getColoredSpanned(popularPlace.getPlace(),R.color.green);
        popularPlaceViewHolder.img_popular_place_view_holder.setImageURI(popularPlace.getImageUrl().get(0));
        popularPlaceViewHolder.txt_popular_place_desc_view_holder.setText(Html.fromHtml(place)+popularPlace.getDescription());
    }
    private String getColoredSpanned(String text,int color) {
        return "<font color=" + color + ">" + text + "</font>";
    }
    @Override
    public int getItemCount() {
        return popularPlacesToShow == null? 0 : popularPlacesToShow.length;
    }

    public void setPopularPlaces(PopularPlace[] popularPlaces) {
        allPopularPlaces = popularPlaces;
        popularPlacesToShow = popularPlaces;
        notifyDataSetChanged();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public PopularPlace[] getPopularPlacesToShow() {
        return popularPlacesToShow;
    }

    class PopularPlaceViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView img_popular_place_view_holder;
        TextView txt_popular_place_desc_view_holder;
        public PopularPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            img_popular_place_view_holder = itemView.findViewById(R.id.img_popular_place_view_holder);
            txt_popular_place_desc_view_holder = itemView.findViewById(R.id.txt_popular_place_desc_view_holder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onRecyclerViewItemClick(getAdapterPosition());
                }
            });
        }
    }
}
