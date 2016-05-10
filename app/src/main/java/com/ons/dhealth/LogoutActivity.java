package com.ons.dhealth;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class LogoutActivity extends AppCompatActivity {

    private static final String TAG = LogoutActivity.class.getSimpleName();
    private ListView listView;
    private User user;
    Bitmap bitmap;
    // contacts JSONArray
    JSONArray contacts = null;


    private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        user = PrefUtils.getCurrentUser(LogoutActivity.this);
        //   profileImage = (ImageView) findViewById(R.id.profileImage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list);

        facebookprofile();
        groupface();
        groupmain();

    }


    public void facebookprofile() {

        // fetching facebook's profile picture
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + "415313148637614" + "/picture?type=large");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //    profileImage.setImageBitmap(bitmap);
            }
        }.execute();

    }

    public void groupface() {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/1483034908681270/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,message,description,updated_time,shares,likes,icon,picture,full_picture");
        request.setParameters(parameters);
        request.executeAsync();

        Bundle param = new Bundle();
        param.putString("fields", "id,picture");
        param.putInt("limit", 100);

//setup a general callback for each graph request sent, this callback will launch the next request if exists.
        final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse response) {

                System.out.println("Print this portion " + response);

            }
        };


    }

    public void groupmain() {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/1483034908681270",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        System.out.println("Print this portion " + response);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,description,owner");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                PrefUtils.clearCurrentUser(LogoutActivity.this);


                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();

                Intent i = new Intent(LogoutActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}