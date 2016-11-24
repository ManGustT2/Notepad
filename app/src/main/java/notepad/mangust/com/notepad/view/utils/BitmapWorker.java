package notepad.mangust.com.notepad.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Администратор on 23.11.2016.
 */
public class BitmapWorker  {

    private static final String TAG = BitmapWorker.class.getSimpleName();

    public static File storeImage(Context context, Bitmap image) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return pictureFile;
    }

    private static File getOutputMediaFile(Context context){
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String mImageName="Image"+ timeStamp +".jpg";
        File file = new File(context.getExternalFilesDir(null), mImageName);
        return file;
    }
}
