package kodeslacker.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;
    public String _picture=null;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public PhotoHandler(Context context) {
        this.context = context;

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/jpeg;base64,");
        sb.append(Base64.encodeBytes(data));
        _picture=sb.toString();
        Log.i("Image:",sb.toString());
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }


}
