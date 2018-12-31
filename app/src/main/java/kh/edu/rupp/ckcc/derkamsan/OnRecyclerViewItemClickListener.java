package kh.edu.rupp.ckcc.derkamsan;

import android.view.View;

public interface OnRecyclerViewItemClickListener {

    void onRecyclerViewItemClick(int position);
    void onRecyclerViewOptionItemClick(int position, View optionView);

}
