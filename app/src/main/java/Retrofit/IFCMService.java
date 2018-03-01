package Retrofit;

import com.example.laptop.ride.Model.FCMResponse;
import com.example.laptop.ride.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Laptop on 2/22/2018.
 */

public interface IFCMService {
    @Headers({
            "content-Type:application/json",
            "Authorization:key=AAAA6vfgnRg:APA91bHXrUbM3Gx_YTyapiKDVFs799rzhJZ8ALLzWo4ncjj56UKIGZq3lEbePv_jbUljJVy6lbzpK4450hF6FfyJepjvSr7CGtyHzruQsgZX9FXxP3RCTLFUsKAqcyeljo4O5Qlt5Zwq"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
