package com.supernover.inamayumunsiclient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.supernover.inamayumunsiclient.Adapter.RecyclerViewAdapter;
import com.supernover.inamayumunsiclient.Model.Upload;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<Upload> uploads;
    private static final String TAG = "MainActivity";
    UpdateManager mUpdateManager;


    private AdView adView;
    // Declare the UpdateManager


    TextView txtFlexibleUpdateProgress;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forceUpdate();





       /* TextView txtCurrentVersion = findViewById(R.id.txt_current_version);
        final TextView txtAvailableVersion = findViewById(R.id.txt_available_version);
        final TextView txtStalenessDays = findViewById(R.id.txt_staleness_days);
        txtFlexibleUpdateProgress = findViewById(R.id.txt_flexible_progress);


        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();

        // Callback from UpdateInfoListener
        // You can get the available version code of the apk in Google Play
        // Number of days passed since the user was notified of an update through the Google Play
        mUpdateManager.addUpdateInfoListener(new UpdateManager.UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                txtAvailableVersion.setText(String.valueOf(code));
            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                txtStalenessDays.setText(String.valueOf(days));
            }

        });
        // Callback from Flexible Update Progress
        // This is only available for Flexible mode
        // Find more from https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible
        mUpdateManager.addFlexibleUpdateDownloadListener(new UpdateManager.FlexibleUpdateDownloadListener() {
            @Override
            public void onDownloadProgress(final long bytesDownloaded, final long totalBytes) {
                txtFlexibleUpdateProgress.setText("Downloading: " + bytesDownloaded + " / " + totalBytes);
            }
        });*/












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
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "*Ubu wakumva Inama y'umunsi nizindi nama zitandukanye zubuzima kuri _Inama_y'Umunsi_ App *\n\n------------------------------\n\n*Download Now* - \n\n" + url);
                        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //startActivity(Intent.createChooser(sharingIntent, "Share using"));

                        PackageManager packageManager = getPackageManager();
                        if (sharingIntent.resolveActivity(packageManager) != null) {
                            startActivity(sharingIntent);
                            // Broadcast the Intent.
                            startActivity(Intent.createChooser(sharingIntent, "Share to"));
                        }



                    }

                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;


        }

        return true;
    }
    /*public void callFlexibleUpdate(View view) {
        // Start a Flexible Update
        mUpdateManager.mode(UpdateManagerConstant.FLEXIBLE).start();
        txtFlexibleUpdateProgress.setVisibility(View.VISIBLE);
    }

    public void callImmediateUpdate(View view) {
        // Start a Immediate Update
        mUpdateManager.mode(UpdateManagerConstant.IMMEDIATE).start();

    }*/
    public void forceUpdate()
    {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,MainActivity.this).execute();
    }


}
