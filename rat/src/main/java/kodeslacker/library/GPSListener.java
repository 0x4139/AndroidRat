package kodeslacker.library;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kodeslacker on 5/30/14.
 */
public class GPSListener {

    private Context ctx;
    private String provider;
    private LocationManager mlocManager;
    private LocationListener listener;
    private int channel ;

    public GPSListener(LocationListener c, String prov, int chan) {
        listener = c;
        provider = prov;
        channel = chan ;
        mlocManager = (LocationManager) ((Context) c).getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates( prov, 0, 0, listener);
    }

    public void stop() {
        if (mlocManager != null) {
            mlocManager.removeUpdates(listener);
        }
    }

    public String getCoordinates(Location loc) {
        JSONObject object = new JSONObject();
        try {
            object.put("Latitude",loc.getLatitude());
            object.put("Longitude", loc.getLongitude());
            object.put("Speed", loc.getSpeed());
            object.put("Accuracy",loc.getAccuracy());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
    public String getCoordinates(){
       Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc==null)
            return "";
       JSONObject object = new JSONObject();
        try {
            object.put("Latitude",loc.getLatitude());
            object.put("Longitude", loc.getLongitude());
            object.put("Speed", loc.getSpeed());
            object.put("Accuracy",loc.getAccuracy());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public int getChannel()
    {
        return channel;
    }

}
