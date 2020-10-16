package com.supernover.inamayumunsiclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        MobileAds.initialize(this,
                "ca-app-pub-3063877521249388~9024543430");

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbarshowsong);
        jcPlayerView = findViewById(R.id.jcplayer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}