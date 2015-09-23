package com.captify.android.captify;

import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Response;


public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "captify://callback";

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "f12b2a9e86a24fbb8c9fe6ef92831aa5";
    // TODO: Replace with your redirect URI


    private String userId = "N/A";


    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer

    private Player mPlayer;
    private SpotifyApi api = new SpotifyApi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-library-read", "playlist-read-collaborative", "playlist-read-private", "user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
//            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
//                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
//                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
//                    @Override
//                    public void onInitialized(Player player) {
//                        mPlayer.addConnectionStateCallback(MainActivity.this);
//                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
//                        mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
//                    }
//                });
//            }
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            TextView textView = (TextView) findViewById(R.id.text_view);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                // Response was successful and contains auth token
                Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                textView.setText("Success");
                //SpotifyApi api = new SpotifyApi();
                api.setAccessToken(response.getAccessToken());

                final SpotifyService spotify = api.getService();
                final String accessToken = response.getAccessToken();
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
//                        .setRequestInterceptor(new RequestInterceptor() {
//                            @Override
//                            public void intercept(RequestFacade request) {
//                                request.addHeader("Authorization", "Bearer " + accessToken);
//                            }
//                        })
//                        .build();

                //SpotifyService spotify = restAdapter.create(SpotifyService.class);
                //UserPrivate user = spotify.getMe();
                final User currentUser = api
//                spotify.getMe(new Callback<UserPrivate>() {
//                    @Override
//                    public void success(UserPrivate user, Response response) {
//                        Log.d("User ID success", user.id);
//                        userId = user.id;
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Log.d("Album failure", error.toString());
//                        userId = "Fail";
//                    }
//                });


//                final String accessToken = response.getAccessToken();
//
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
//                        .setRequestInterceptor(new RequestInterceptor() {
//                            @Override
//                            public void intercept(RequestInterceptor.RequestFacade request) {
//                                request.addHeader("Authorization", "Bearer " + accessToken);
//                            }
//                        })
//                        .build();
//
//                SpotifyService spotify = restAdapter.create(SpotifyService.class);

                // Most (but not all) of the Spotify Web API endpoints require authorization.
                // If you know you'll only use the ones that don't require authorisation you can skip this step
                //UserPrivate userPrivate = spotify.getMe();
                //Pager<PlaylistSimple> myPlaylists = spotify.getPlaylists(myUser.id);

                //textView.setText("Success" + " - "+user.id);


            }
            else if(response.getType() == AuthenticationResponse.Type.ERROR) {
                Toast.makeText(getApplicationContext(), "Login Error!", Toast.LENGTH_LONG).show();
                textView.setText("Fail");
                //break;
            }
            else{

                // Most likely auth flow was cancelled
                //default:
                    // Handle other cases
                    Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_LONG).show();
                    textView.setText("Default");

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}