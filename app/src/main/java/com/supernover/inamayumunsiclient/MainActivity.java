package com.supernover.inamayumunsiclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supernover.inamayumunsiclient.Adapter.RecyclerViewAdapter;
import com.supernover.inamayumunsiclient.Model.Upload;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<Upload> uploads;
    private static final String TAG = "MainActivity";



    private AdView adView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();
        progressDialog.setMessage("please wait ...");
        progressDialog.show();





        MobileAds.initialize(this, "ca-app-pub-3063877521249388~9024543430");
        adView = (AdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        //AdSize adSize = new AdSize(300, 50);


        adView.setAdListener(new AdListener() {
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
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
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
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                for (DataSnapshot postsnaphot : dataSnapshot.getChildren()){
                    Upload upload = postsnaphot.getValue(Upload.class);
                    uploads.add(upload);
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(),uploads);


                AdmobNativeAdAdapter admobNativeAdAdapter= AdmobNativeAdAdapter.Builder
                 .with(
                 "ca-app-pub-3063877521249388/8819232133",//Create a native ad id from admob console
                 adapter,//The adapter you would normally set to your recyClerView
                         "custom"
                 )
                 .adItemIterval(2)//native ad repeating interval in the recyclerview
                 .build();
                //and set admobNativeAdAdapter in place of  youradapter
                recyclerView.setAdapter(admobNativeAdAdapter);//set your RecyclerView adapter with the admobNativeAdAdapter
                //recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Rate:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getString(R.string.packegname)));
                startActivity(intent);

                break;
            default:
              //  break;
            case R.id.sharemenu:


                FirebaseDatabase.getInstance().getReference().child("ver").child("updatelink").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue().toString();

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);


                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "*\uD835\uDC14\uD835\uDC1B\uD835\uDC2E \uD835\uDC30\uD835\uDC1A\uD835\uDC24\uD835\uDC2E\uD835\uDC26\uD835\uDC2F\uD835\uDC1A \uD835\uDC08\uD835\uDC27\uD835\uDC1A\uD835\uDC26\uD835\uDC1A \uD835\uDC32'\uD835\uDC2E\uD835\uDC26\uD835\uDC2E\uD835\uDC27\uD835\uDC2C\uD835\uDC22 \uD835\uDC27\uD835\uDC22\uD835\uDC33\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC22 \uD835\uDC27\uD835\uDC1A\uD835\uDC26\uD835\uDC1A \uD835\uDC33\uD835\uDC22\uD835\uDC2D\uD835\uDC1A\uD835\uDC27\uD835\uDC1D\uD835\uDC2E\uD835\uDC24\uD835\uDC1A\uD835\uDC27\uD835\uDC32\uD835\uDC1E \uD835\uDC33\uD835\uDC2E\uD835\uDC1B\uD835\uDC2E\uD835\uDC33\uD835\uDC22\uD835\uDC26\uD835\uDC1A \uD835\uDC1C\uD835\uDC32\uD835\uDC1A\uD835\uDC27\uD835\uDC20\uD835\uDC30\uD835\uDC1A \uD835\uDC22\uD835\uDC1B\uD835\uDC22\uD835\uDC20\uD835\uDC1A\uD835\uDC27\uD835\uDC22\uD835\uDC2B\uD835\uDC28 \uD835\uDC1B\uD835\uDC22\uD835\uDC20\uD835\uDC1E\uD835\uDC33\uD835\uDC30\uD835\uDC1E\uD835\uDC21\uD835\uDC28 \uD835\uDC21\uD835\uDC1A\uD835\uDC2B\uD835\uDC22\uD835\uDC26\uD835\uDC28 \uD835\uDC2C\uD835\uDC28\uD835\uDC1B\uD835\uDC1A\uD835\uDC27\uD835\uDC2E\uD835\uDC24\uD835\uDC22\uD835\uDC2B\uD835\uDC30\uD835\uDC1A,\uD835\uDC1B\uD835\uDC1A\uD835\uDC33\uD835\uDC1A \uD835\uDC2C\uD835\uDC21\uD835\uDC1A\uD835\uDC27\uD835\uDC20\uD835\uDC1A\uD835\uDC33\uD835\uDC22 ,\uD835\uDC22\uD835\uDC23\uD835\uDC1A\uD835\uDC26\uD835\uDC1B\uD835\uDC28 \uD835\uDC11\uD835\uDC32\uD835\uDC1A\uD835\uDC21\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC2E\uD835\uDC2B\uD835\uDC1A \uD835\uDC14\uD835\uDC1B\uD835\uDC2E\uD835\uDC33\uD835\uDC22\uD835\uDC26\uD835\uDC1A,\uD835\uDC22\uD835\uDC1B\uD835\uDC22\uD835\uDC2B\uD835\uDC1A\uD835\uDC2B\uD835\uDC22 \uD835\uDC1B\uD835\uDC32\uD835\uDC2E \uD835\uDC1B\uD835\uDC2E\uD835\uDC2D\uD835\uDC1E\uD835\uDC20\uD835\uDC1E\uD835\uDC2D\uD835\uDC2C\uD835\uDC22 \uD835\uDC27\uD835\uDC22\uD835\uDC1B\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC22 \uD835\uDC1B\uD835\uDC32\uD835\uDC22\uD835\uDC27\uD835\uDC2C\uD835\uDC21\uD835\uDC22 \uD835\uDC24\uD835\uDC2E\uD835\uDC2B\uD835\uDC22 _\uD835\uDC08\uD835\uDC27\uD835\uDC1A\uD835\uDC26\uD835\uDC1A_\uD835\uDC32'\uD835\uDC14\uD835\uDC26\uD835\uDC2E\uD835\uDC27\uD835\uDC2C\uD835\uDC22_ \uD835\uDC00\uD835\uDC29\uD835\uDC29 *\n" +
                                "\n" +
                                "------------------------------\n" +
                                "Share& Give us feedback on playstore\n" +
                                "\n" +
                                "Inkunga ikomeye cyane.\n" +
                                "*\uD835\uDC03\uD835\uDC28\uD835\uDC30\uD835\uDC27\uD835\uDC25\uD835\uDC28\uD835\uDC1A\uD835\uDC1D \uD835\uDC0D\uD835\uDC28\uD835\uDC30* - \n" +
                                "\n" +
                                "Urakoze. \n\n" + url);
                        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        PackageManager packageManager = getPackageManager();
                        if (sharingIntent.resolveActivity(packageManager) != null) {
                            startActivity(sharingIntent);
                            // Broadcast the Intent.
                            startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                        }



                    }

                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;


        }

        return true;
    }
}
