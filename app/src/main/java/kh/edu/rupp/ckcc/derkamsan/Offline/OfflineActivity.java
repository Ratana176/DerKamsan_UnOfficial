package kh.edu.rupp.ckcc.derkamsan.Offline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import kh.edu.rupp.ckcc.derkamsan.LibraryHelper;
import kh.edu.rupp.ckcc.derkamsan.OnRecyclerViewItemClickListener;
import kh.edu.rupp.ckcc.derkamsan.R;

public class OfflineActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        Toolbar toolbar =findViewById(R.id.offline_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.offline_recyclerview_layout);
        int numberOfCol = LibraryHelper.NoOfColumns(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,numberOfCol);
        recyclerView.setLayoutManager(layoutManager);

        OfflineAdapter offlineAdapter = new OfflineAdapter();
        recyclerView.setAdapter(offlineAdapter);
        offlineAdapter.setOnRecyclerViewItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerViewItemClick(int position) {

    }

    @Override
    public void onRecyclerViewOptionItemClick(int position, View optionView) {

    }
}
