package com.example.hackernam.smartcontact;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterResponse;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    public static final String PREFS_NAME = "MyPrefsFile";

    //Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private int FB_REQUEST_CODE;
    private String FB_USER_ID;
    //Google+
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    //Twitter
    private static final String PREF_NAME = "sample_twitter_pref";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_USER_NAME = "twitter_user_name";

    public static final int WEBVIEW_REQUEST_CODE = 200;
    private static Twitter twitter;
    private static RequestToken requestToken;

    private Button twitterbutton;
    private static SharedPreferences sharedPreferences;

    private String consumerKey = null;
    private String consumerSecret = null;
    private String callbackUrl = null;
    private String oAuthVerifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int GiaoDien = 0;
        SharedPreferences sharedPreferences= this.getSharedPreferences("CaiDatGiaoDien", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            GiaoDien = sharedPreferences.getInt("MaGiaoDien", 2);
        }
        else
        {
            GiaoDien = 1;
        }
        if(GiaoDien == 1) {
            setTheme(R.style.AppActionBarColor1);
        }
        if(GiaoDien == 2) {
            setTheme(R.style.AppActionBarColor2);
        }
        if(GiaoDien == 3) {
            setTheme(R.style.AppActionBarColor3);
        }
        if(GiaoDien == 4) {
            setTheme(R.style.AppActionBarColor4);
        }
        if(GiaoDien == 5) {
            setTheme(R.style.AppActionBarColor5);
        }
        if(GiaoDien == 6) {
            setTheme(R.style.AppActionBarColor6);
        }
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        initTwitterConfigs();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Facebook
        FacebookCallLogin();

        //Google+
        GooglePlusCallLogin();

        //Twitter
        TwitterCallLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Facebook
        if (requestCode == FB_REQUEST_CODE){
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }

        //Google+
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
            return;
        }

        //Twitter
        if(resultCode == Activity.RESULT_OK) {
            String verifier = data.getExtras().getString(oAuthVerifier);

            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                long userId = accessToken.getUserId();
                final User user = twitter.showUser(userId);
                String username = user.getName();

                saveTwitterInfo(accessToken);

                setInfo(username, "bvtien04@gmail.com", user.getMiniProfileImageURL(), "", String.valueOf(userId), "");

                twitterbutton.setText("Log out Twitter");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            //Calling signin google+
            signIn();
        }else if (v.getId() == R.id.btn_login){
            //Twitter
            if(sharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false)){
                //Da dang nhap
                logoutFromTwitter();
                return;
            }
            loginToTwitter();
        }
    }

    private void setInfo(String username, String email, String imagepath, String fbid, String twid, String gpid){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String Username = settings.getString("username", "");
        String Email = settings.getString("email", "");
        String Imagepath = settings.getString("imagepath", "");
        String FBid = settings.getString("fbid", "");

        SharedPreferences.Editor editor = settings.edit();
        if (!username.isEmpty()){
            editor.putString("username", username);
        }
        if (!email.isEmpty()){
            editor.putString("email", email);
        }
        if (!imagepath.isEmpty()){
            editor.putString("imagepath", imagepath);
        }
        if (!fbid.isEmpty()){
            editor.putString("fbid", fbid);
        }
        editor.commit();
        setResult(Activity.RESULT_OK);
        finish();
    }

//  Facebook
    private void FacebookCallLogin() {

    loginButton = (LoginButton)findViewById(R.id.login_button);
    FB_REQUEST_CODE = loginButton.getRequestCode();
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Insert your code here
                            String fbid = "";
                            String username = "";
                            String imagepath= "";
                            String email = "";
                            try {
                                fbid = object.getString("id");
                                username = object.getString("name");
                                imagepath = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                email = object.getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setInfo(username, email , imagepath, fbid, "", "");
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,picture,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            //info.setText("Login attempt canceled.");
        }

        @Override
        public void onError(FacebookException e) {
            //info.setText("Login attempt failed.");
        }
    });
}

//  Google Sign in
    private void GooglePlusCallLogin() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        signInButton = ((SignInButton) findViewById(R.id.sign_in_button));

        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setPadding(0, 0, 20, 0);
                return;
            }
        }
        signInButton.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

        signInButton.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Displaying name and email
//            textViewName.setText(acct.getDisplayName());
//            textViewEmail.setText(acct.getEmail());

            //Initializing image loader
//            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                    .getImageLoader();

//            imageLoader.get(acct.getPhotoUrl().toString(),
//                    ImageLoader.getImageListener(profilePhoto,
//                            R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher));
//
//            //Loading image
//            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Ket noi that bai", Toast.LENGTH_SHORT);
    }

//  Twitter
    private void initTwitterConfigs() {
        consumerKey = getString(R.string.twitter_consumer_key);
        consumerSecret = getString(R.string.twitter_consumer_secret);
        callbackUrl = getString(R.string.twitter_callback);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);
    }

    private void loginToTwitter() {

        boolean isLoggedIn = sharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        if(!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKey);
            builder.setOAuthConsumerSecret(consumerSecret);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();


            try {
                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            //loginLayout.setVisibility(View.GONE);
            //shareLayout.setVisibility(View.VISIBLE);
        }
    }

    private void logoutFromTwitter() {
        // Clear the shared preferences
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();

        twitterbutton.setText("Login to Twitter");
    }

    private void saveTwitterInfo(AccessToken accessToken) {

        long userId = accessToken.getUserId();
        User user;

        try {

            user = twitter.showUser(userId);
            String username = user.getName();

            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void TwitterCallLogin() {
        twitterbutton = (Button)findViewById(R.id.btn_login);
        twitterbutton.setOnClickListener(this);

        if(TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
            Toast.makeText(this, "Twitter key or secret not configured",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, 0);

        boolean isLoggedIn = sharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        if(isLoggedIn) {
            //Da dang nhap twitter

            twitterbutton.setText("Log out Twitter");
            String username = sharedPreferences.getString(PREF_USER_NAME, "");
            //userName.setText(getResources().getString(R.string.hello) + " " + username);
        } else {

            Uri uri = getIntent().getData();

            if(uri != null && uri.toString().startsWith(callbackUrl)) {

                String verifier = uri.getQueryParameter(oAuthVerifier);

                try {

                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                    long userId = accessToken.getUserId();
                    final User user = twitter.showUser(userId);
                    final String username = user.getName();

                    saveTwitterInfo(accessToken);
                    //userName.setText(getString(R.string.hello) + " " + username);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
