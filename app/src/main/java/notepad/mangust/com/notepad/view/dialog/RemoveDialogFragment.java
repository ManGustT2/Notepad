package notepad.mangust.com.notepad.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface.OnClickListener;

import notepad.mangust.com.notepad.R;

/**
 * Created by Администратор on 20.10.2016.
 */
public class RemoveDialogFragment extends DialogFragment implements OnClickListener {

    public static final String TAG_REMOVE_SELECTED = "weight";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Delete data").setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setMessage(R.string.message_text);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                Intent intent = new Intent();
                intent.putExtra(TAG_REMOVE_SELECTED, true);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                break;
            case Dialog.BUTTON_NEGATIVE:
                onDismiss(dialog);
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}

