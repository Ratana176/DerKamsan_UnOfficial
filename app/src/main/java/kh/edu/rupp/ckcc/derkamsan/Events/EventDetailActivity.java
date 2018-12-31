package kh.edu.rupp.ckcc.derkamsan.Events;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kh.edu.rupp.ckcc.derkamsan.ImageSlider.ViewPagerAdapter;
import kh.edu.rupp.ckcc.derkamsan.LibraryHelper;
import kh.edu.rupp.ckcc.derkamsan.R;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private Toolbar toolbar;
    private TextView txt_event_title_details;
    private TextView txt_event_date_details;
    private LinearLayout btn_event_details_download;
    private LinearLayout btn_event_details_like;
    private TextView txt_event_details_title;
    private TextView txt_event_details_desc;
    private TextView txt_event_details_title1;
    private TextView txt_event_details_desc1;
    private Event event;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        toolbar = findViewById(R.id.event_toolbar);
        txt_event_title_details = findViewById(R.id.txt_event_title_details);
        txt_event_date_details = findViewById(R.id.txt_event_date_details);
        btn_event_details_download = findViewById(R.id.btn_event_details_download);
        btn_event_details_like = findViewById(R.id.btn_event_details_like);
        txt_event_details_title = findViewById(R.id.txt_event_details_title);
        txt_event_details_desc = findViewById(R.id.txt_event_details_desc);
        txt_event_details_title1 = findViewById(R.id.txt_event_details_title1);
        txt_event_details_desc1 = findViewById(R.id.txt_event_details_desc1);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        String eventJson = getIntent().getStringExtra("eventJson");
        Gson gson = new Gson();
        event = gson.fromJson(eventJson, Event.class);

        txt_event_title_details.setText(event.getTitle());
        txt_event_date_details.setText(event.getDate());

        btn_event_details_download.setOnClickListener(this);
        btn_event_details_like.setOnClickListener(this);

        txt_event_details_title.setText(event.getTitle());
        txt_event_details_desc.setText(event.getDescription());

        txt_event_details_title1.setText(event.getTitle());
        txt_event_details_desc1.setText(event.getTitle());
        imagesSlider();


    }
    private void imagesSlider() {

        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, event.getImageUrl());
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 6000);
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            EventDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < dotscount - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_event_details_download:
                DownloadTask task = new DownloadTask();
                task.execute(event.getImageUrl());
                break;
            case R.id.btn_event_details_like:
                LibraryHelper.showToast("Btn Like clicked",getApplicationContext());
                break;
        }
    }
    class DownloadTask extends AsyncTask<List<String>,Integer,String>{
        private int file_length = 0;
        private int progress = 0;
        private int totalProgress = 0;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EventDetailActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(10);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(EventDetailActivity.this,result,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(List<String>... urls) {
            File newFolder = new File(Environment.getExternalStorageDirectory().getPath()+"/Derkamsan");
            if (!newFolder.exists()){
                newFolder.mkdir();
            }
            File nextFolder = new File(Environment.getExternalStorageDirectory().getPath()+"/Derkamsan/Images");
            if (!nextFolder.exists()){
                nextFolder.mkdir();
            }
            String[] paths = new String[urls[0].size()];
            for (int i=0;i<paths.length;i++){
                paths[i] =urls[0].get(i);
            }
            int i=0;
            URL url;
            InputStream inputStream;
            OutputStream outputStream;
            for(String path: paths){
                progress = 0;
                try {
                    url = new URL(path);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    file_length+= connection.getContentLength();

                    File newImage = new File(nextFolder,event.getId()+i+".jpg"); i++;
                    inputStream = new BufferedInputStream(url.openStream(),8192);
                    byte[] data = new byte[1024];
                    int total =0;
                    int count =0;
                    outputStream = new FileOutputStream(newImage);
                    while ((count=inputStream.read(data))!=-1){
                        total+=count;
                        progress = (total*100/file_length);
                        outputStream.write(data,0,count);
                        totalProgress+=progress;
                        publishProgress(progress);
                    }

                    outputStream.close();
                    inputStream.close();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (i==paths.length)
                return "Downloaded";
            return "Can't Download.";
        }
    }
}
