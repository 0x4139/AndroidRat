package kodeslacker.library;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kodeslacker on 5/30/14.
 */
public class SystemInfo {
    Context ctx;
    TelephonyManager tm;

    public SystemInfo(Context c) {
        ctx = c;
        tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private String getPhoneNumber() {
        return tm.getLine1Number();
    }

    private String getIMEI() {
        return tm.getDeviceId();
    }

    private String getCountryCode() {
        return tm.getNetworkCountryIso();
    }

    private String getOperatorName() {
        return tm.getNetworkOperatorName();
    }

    private String getSimCountryCode() {
        return tm.getSimCountryIso();
    }

    private String getSimOperatorCode() {
        return tm.getSimOperator();
    }

    private String getSimSerial() {
        return tm.getSimSerialNumber();
    }

    public String getSystemInfo() {
        JSONObject object = new JSONObject();
        try {
            object.put("IMEI",getIMEI());
            object.put("Manufacturer", Build.MANUFACTURER);
            object.put("Model", Build.MODEL);
            object.put("CountryCode",getCountryCode());
            object.put("Operator",getOperatorName());
            object.put("SimCountry",getSimCountryCode());
            object.put("SimOperator",getSimOperatorCode());
            object.put("SimSerial",getSimSerial());
            object.put("PhoneNumber", getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }


}

