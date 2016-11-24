package notepad.mangust.com.notepad.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
        //do your logic
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notifyListeners(imageUri);
                }
            }
        }).start();
    }

    private synchronized void notifyListeners(final Uri imageUri) {
        //// TODO: 24.11.16 перепеши на broadcastы
    }

    public class ImageSaverServiceBinder extends Binder {
        public ImageSaverService getService() {
            return ImageSaverService.this;
        }
    }
}
