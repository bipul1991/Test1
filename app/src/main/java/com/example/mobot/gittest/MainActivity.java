package com.example.mobot.gittest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    ImageView profileImg;
    Button logoutBtn;

    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Helooooooooooooooooooo
        //;;;;;;

        loginButton = (LoginButton)findViewById(R.id.login_button);
        profileImg = (ImageView)findViewById(R.id.profileImg);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);

        FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String userId = loginResult.getAccessToken().getUserId();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("graphShow: ",".."+object);

                        displayUserInfo(object);

                     //   Log.d("graphShow: ",".."+object);

                    }
                });
            Bundle parameters= new Bundle();
                parameters.putString("fields","first_name, last_name, email, id, gender, cover, picture.type(large)");
                graphRequest.setParameters(parameters);

                graphRequest.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


logoutBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        LoginManager.getInstance().logOut();


        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }
});

    }




    public  void displayUserInfo(JSONObject object)
    {

        String firstName,lastName,email,id;

        try {
            firstName = object.getString("first_name");
            lastName = object.getString("last_name");
            email = object.getString("email");
            id=object.getString("id");

          /*  if (object.has("picture")) {
                String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
               Log("pImg",".."+profilePicUrl)

            }*/

            if (object.has("picture")) {
                String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                Log.i("pimg",".."+profilePicUrl);
            }

            Log.d("firstName: ",".."+firstName);
            Log.d("lastName: ",".."+lastName);
            Log.d("email: ",".."+email);
            Log.d("id: ",".."+id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
