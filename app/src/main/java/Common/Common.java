package Common;

import com.google.android.gms.maps.internal.IGoogleMapDelegate;

import Retrofit.IGoogleAPI;
import Retrofit.RetrofitClient;

/**
 * Created by Laptop on 12/9/2017.
 */

public class Common {
    public static final String driver_tbl = "Drivers";
    public static final String user_driver_tbl = "Driversinformation";
    public static final String user_rider_tbl = "RidersInformation";
    public static final String pickup_request_tbl = "Pickuprequest";


    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
