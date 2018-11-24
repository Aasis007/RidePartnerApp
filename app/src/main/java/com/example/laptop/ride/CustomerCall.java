package com.example.laptop.ride;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.ride.Model.FCMResponse;
import com.example.laptop.ride.Model.Notification;
import com.example.laptop.ride.Model.Sender;
import com.example.laptop.ride.Model.Token;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import Common.Common;
import Retrofit.IFCMService;
import Retrofit.IGoogleAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCall extends AppCompatActivity {

    TextView Time, Address, Distance;
    MediaPlayer mediaPlayer;

    IGoogleAPI mServices;
    IFCMService mFCMService;
    Button acceptBtn,declinebtn;
    String customerID;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);

        mServices = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();

        //initialize views

        Address = (TextView) findViewById(R.id.txtAddress);
        Distance = (TextView) findViewById(R.id.txtDistance);
        Time = (TextView) findViewById(R.id.txtTime);

        //buttons
        acceptBtn = (Button)findViewById(R.id.btnacept);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerCall.this,RiderTracking.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("customerID",customerID);
                startActivity(intent);
                finish();
            }
        });
        declinebtn = (Button)findViewById(R.id.declinebtn);

        declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(customerID))
                        cancelbooking();

            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {
           lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng", 0);

            customerID = getIntent().getStringExtra("customer");

            getDirection(lat, lng);


        }


    }

    private void cancelbooking() {
        Token token = new Token(customerID);
        Notification notification = new Notification("Cancel","Partner has declined your ride request");
        Sender sender = new Sender(token.getToken(),notification);

        mFCMService.sendMessage(sender)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1)
                        {
                            Toast.makeText(CustomerCall.this, "Request Cancelled",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    private void getDirection(double lat, double lng) {


        String requestAPI = null;
        try {
            requestAPI = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + Common.mLastLocation.getLatitude() + "," + Common.mLastLocation.getLongitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("Rideapp", requestAPI);//Prinnt URL for debug
            mServices.getPath(requestAPI)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                //get 1st element of route
                                JSONObject jsonObject = new JSONObject(response.body().toString());

                                JSONArray routes = jsonObject.getJSONArray("routes");

                                JSONObject object = routes.getJSONObject(0);

                                //get legs
                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject legsObject = legs.getJSONObject(0);
                                //get distance
                                JSONObject distance = legsObject.getJSONObject("distance");
                                Distance.setText(distance.getString("text"));

                                //get time
                                JSONObject time = legsObject.getJSONObject("duration");
                                Time.setText(time.getString("text"));

                                //get Address
                                String address = legsObject.getString("end_address");
                                Address.setText(address);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(CustomerCall.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mediaPlayer.start();
    }
}




