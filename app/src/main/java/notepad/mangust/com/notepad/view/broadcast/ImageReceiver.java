package notepad.mangust.com.notepad.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import notepad.mangust.com.notepad.view.fragments.NoteEnterFragment;

public class ImageReceiver extends BroadcastReceiver {
    public ImageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NoteEnterFragment noteEnterFragment = new NoteEnterFragment();
        noteEnterFragment.onImagegSaved(intent.getStringExtra("imageUri"));
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
