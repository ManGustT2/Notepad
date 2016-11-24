package notepad.mangust.com.notepad.view.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.File;
import java.util.Date;

import io.realm.Realm;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.service.ImageSaverService;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

public class NoteEnterFragment extends BaseFragment {
    static final int GALLERY_REQUEST = 1;
    private static String ENTER_KEY = "enterkey";
    private NoteActivity mNoteActivity;
    private EditText mEditTextTitle;
    private EditText mEditTextEnter;
    private Note mNote;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private ImageSaverService mImageSaverService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mImageSaverService = ((ImageSaverService.ImageSaverServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mImageSaverService = null;
        }
    };
    // TODO: 24.11.16 save instanceState
    private Uri mSelectedImageUri;
    private ProgressDialog mProgress;
    private Uri mSavedUri;

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static NoteEnterFragment getInstance(Note note) {
        Assert.assertNotNull("Note is null", note);
        NoteEnterFragment noteEnterFragment = new NoteEnterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ENTER_KEY, note);
        noteEnterFragment.setArguments(bundle);
        return noteEnterFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoteActivity = (NoteActivity) context;
        if (getArguments() != null) {
            mNote = getArguments().getParcelable(ENTER_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_note, container, false);
        findUI(v);
        setHasOptionsMenu(true);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(new Intent(getActivity(), ImageSaverService.class), mConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(mConn);
        hideKeyboard(getActivity(), getView());
    }

    // TODO: 11.11.2016 permission;
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    // TODO: 18.11.16 Try urcop lib
                    mSelectedImageUri = imageReturnedIntent.getData();
//                    mBitmap = BitmapWorker.decodeFile(getPath(selectedImage));
                    mImageView.setImageURI(mSelectedImageUri);
            }
        }
    }

    // TODO: 18.11.16 move to util class
//    private Bitmap decodeFile(String imgPath) {
//        Bitmap b = null;
//        int max_size = 10000;
//        File f = new File(imgPath);
//        try {
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            FileInputStream fis = new FileInputStream(f);
//            BitmapFactory.decodeStream(fis, null, o);
//            fis.close();
//            int scale = 1;
//            if (o.outHeight > max_size || o.outWidth > max_size) {
//                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
//            }
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            fis = new FileInputStream(f);
//            b = BitmapFactory.decodeStream(fis, null, o2);
//            fis.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mBitmap = b;
//
//        return b;
//    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (checkData()) {
                    if (mNote == null) {
                        createObject();
                    } else {
                        editObject();
                    }
                } else {
                    Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkData() {
        return mEditTextEnter.getText().toString().trim().length() > 0
                && mEditTextTitle.getText().toString().trim().length() > 0;
    }

    private void findUI(View view) {
        mNoteActivity.setTitle("New Note");
        mEditTextEnter = (EditText) view.findViewById(R.id.enterTextNEF);
        mEditTextTitle = (EditText) view.findViewById(R.id.titleNEF);
        mImageView = (ImageView) view.findViewById(R.id.imageViewNEF);
        if (mNote != null) {
            mEditTextTitle.setText(mNote.getmTitle());
            mEditTextEnter.setText(mNote.getDescriptionTV());
            mImageView.setImageURI(Uri.parse(new File(mNote.getUri()).toString()));
        }
    }

    private void createObject() {
        if (mSelectedImageUri != null) {
            if (mImageSaverService != null) {
                mImageSaverService.saveImage(mSelectedImageUri);
                mProgress = new ProgressDialog(getContext());
                mProgress.setIndeterminate(true);
                mProgress.setTitle("Saving...");
                mProgress.show();
            } else {
                Toast.makeText(getActivity(), "Internal error", Toast.LENGTH_LONG).show();
            }
        } else {
            saveNote();
        }
//        String path = Uri.fromFile(BitmapWorker.storeImage(getActivity(), mBitmap)).toString();
//        mRealm.beginTransaction();
//        int key;
//        try {
//            key = mRealm.where(Note.class).findAll().size() + 1;
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            key = 0;
//        }
//
//
//        hideKeyboard(getActivity(), getView());
//        mNoteActivity.onBackPressed();
//        mRealm.close();
    }

    private void editObject() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(getDataNote(mNote));
        realm.commitTransaction();
        realm.close();
        mNoteActivity.onBackPressed();
    }

    private Note getDataNote(Note note) {
        note.setmTitle(mEditTextTitle.getText().toString());
        note.setDescriptionTV(mEditTextEnter.getText().toString());
        note.setmDate(new Date());
        return note;
    }


    public void onImageSaved(final Uri savedImageUri) {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSavedUri = savedImageUri;
                    saveNote();
                    if (mProgress != null) {
                        mProgress.dismiss();
                    }
                    Toast.makeText(getContext(), "Note successfully saved", Toast.LENGTH_LONG).show();
                    mNoteActivity.onBackPressed();
                }
            });
        }
    }

    private void saveNote() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int key;
        try {
            key = realm.where(Note.class).findAll().size() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }
        Note note = new Note();
        note = getDataNote(note);
        if (mSavedUri != null) {
            note.setUri(mSavedUri.toString());
        }
        note.setId(key);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
        realm.close();
    }
}
