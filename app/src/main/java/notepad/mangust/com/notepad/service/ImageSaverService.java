package notepad.mangust.com.notepad.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;

import notepad.mangust.com.notepad.view.fragments.NoteEnterFragment;
import notepad.mangust.com.notepad.view.utils.BitmapWorker;

/**
 * Tiply
 * Created by rostyslav on 24.11.16
 */
public class ImageSaverService extends Service {

    private ImageSaverServiceBinder mImageSaverServiceBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mImageSaverServiceBinder = new ImageSaverServiceBinder();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mImageSaverServiceBinder;
    }

    public void saveImage(final Uri imageUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (Thread.currentThread()) {
                    File file = BitmapWorker.storeImage(getApplicationContext(),
                            BitmapWorker.decodeFile(imageUri.toString()));
                    notifyListeners(Uri.fromFile(file));
                }
            }
        }).start();
    }

    private synchronized void notifyListeners(final Uri imageUri) {
        Intent intent= new Intent(NoteEnterFragment.BROADCAST_ACTION);
        intent.putExtra("uri", imageUri);
        sendBroadcast(intent);
    }

    public class ImageSaverServiceBinder extends Binder {
        public ImageSaverService getService() {
            return ImageSaverService.this;
        }
    }
}
