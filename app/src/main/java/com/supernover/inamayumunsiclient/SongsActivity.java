package com.supernover.inamayumunsiclient;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supernover.inamayumunsiclient.Adapter.JcSongsAdapter;
import com.supernover.inamayumunsiclient.Model.GetSongs;

import java.util.ArrayList;
import java.util.List;

public class SongsActivity extends AppCompatActivity {


    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref;//For saving sort setting

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Boolean checkin = false;
    List<GetSongs> mupload;
    JcSongsAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentIndex;
    private AdView mAdView;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        mSharedPref = getSharedPreferences("SortSetting",MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort","newest");// where if there i s no  setting  selected
        if (mSorting.equals("newest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from  bottom means newest things

            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

        }
        else if (mSorting.equals("oldest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from  bottom means oldest things
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false)
            ;
        }

        MobileAds.initialize(this,
                "ca-app-pub-3063877521249388~9024543430");

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbarshowsong);
        jcPlayerView = findViewById(R.id.jcplayer);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mLayoutManager);

        mupload = new ArrayList<>();
        recyclerView.setAdapter(adapter);


        interstitialAd = new InterstitialAd(this);

        interstitialAd.setAdUnitId("ca-app-pub-3063877521249388/6604788870");

        interstitialAd.loadAd(new AdRequest.Builder().build());


            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });

        adapter = new JcSongsAdapter(getApplicationContext(), mupload, new JcSongsAdapter.RecyclerItemClickLister() {
            @Override
            public void onClickListener(GetSongs songs, int position) {

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                changeSelectedSong(position);


                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();




            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mupload.clear();
                for (DataSnapshot dss: dataSnapshot.getChildren()){
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setMkey(dss.getKey());
                    currentIndex = 0;
                    final String s = getIntent().getExtras().getString("songsCategory");
                    if (s.equals(getSongs.getSongsCategory())){
                        mupload.add(getSongs);
                        checkin = true;
                        jcAudios.add(JcAudio.createFromURL(getSongs.getSongTitle(),getSongs.getSongLink()));

                    }
                }
                adapter.setSeletedPosition(0);



                /**AdmobNativeAdAdapter admobNativeAdAdapter= AdmobNativeAdAdapter.Builder
                        .with(
                                "ca-app-pub-3940256099942544/2247696110",//Create a native ad id from admob console
                                adapter,//The adapter you would normally set to your recyClerView
                                "small"
                        )
                        .adItemIterval(2)//native ad repeating interval in the recyclerview
                        .build();**/

                //and set admobNativeAdAdapter in place of  youradapter
                recyclerView.setAdapter(adapter);//set your RecyclerView adapter with the admobNativeAdAdapter
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (checkin){
                    jcPlayerView.initPlaylist(jcAudios,null);

                }else {
                    Toast.makeText(SongsActivity.this,"Nta kintu gihari", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);




            }
        });




    }
    public void changeSelectedSong(int index){
        adapter.notifyItemChanged(adapter.getSeletedPosition());
        currentIndex = index;
        adapter.setSeletedPosition(currentIndex);
        adapter.notifyItemChanged(currentIndex);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tondekanya, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort){

            showSortDialog();
            //kugaragaza alert to chose sorting
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void showSortDialog(){
        // ibigomba kugaragara muriryo
        String [] sortOptions = {"BISHYASHYA(Newest)","BISHAJE(Oldest)"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("TONDEKANYA UHEREYE KU")//set title
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i ==0){
                            //sort by  newest
                            //edit  our shared preferences

                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","newest"); //where sort  is key  newest is  value
                            editor.apply();//apply/save values in  shared preferences
                            recreate();

                        }
                        else if (i == 1){{

                            //sort by  newest
                            //edit  our shared preferences

                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","oldest"); //where sort  is key  oldest is  value
                            editor.apply();//apply/save values in  shared preferences
                            recreate();

                        }}
                    }
                });
        builder.show();


    }
}