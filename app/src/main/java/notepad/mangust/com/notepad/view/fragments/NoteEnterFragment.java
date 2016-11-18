package notepad.mangust.com.notepad.view.fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.FileNotFoundException;
import java.util.Date;
import io.realm.Realm;
import io.realm.internal.IOException;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

public class NoteEnterFragment extends BaseFragment {
    static final int GALLERY_REQUEST = 1;
    private NoteActivity mNoteActivity;
    private EditText mEditTextTitle;
    private EditText mEditTextEnter;
    private Note mNote;
    private Realm mRealm;
    private ImageView mImageView;

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoteActivity = (NoteActivity) context;
        if (getArguments() != null)
            mNote = (Note) getArguments().getParcelable(NoteDetailFragment.ENTER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_note, container, false);
        mRealm = Realm.getDefaultInstance();
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
            Bitmap bitmap = null;
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                    mImageView.setImageBitmap(bitmap);
            }
        }
    }

    private void setPic(String imagePath, ImageView destination){
        int targetW = destination.getWidth();
        int targetH = destination.getHeight();

        BitmapFactory.Options bmOption = new BitmapFactory.Options();
        bmOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOption);
        int photoW = bmOption.outWidth;
        int photoH = bmOption.outHeight;

       int scaleFact = Math.min(photoW/targetW, photoH/targetH);

        bmOption.inJustDecodeBounds = false;
        bmOption.inSampleSize = scaleFact;
        bmOption.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOption);
        destination.setImageBitmap(bitmap);
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
                if (mEditTextEnter.getText().toString().trim().length() > 0
                        && mEditTextTitle.getText().toString().trim().length() > 0) {
                    if (mNote == null) {
                        greateObject();
                    } else {
                        editObject();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findUI(View view) {
        mNoteActivity.setTitle("New Note");
        mEditTextEnter = (EditText) view.findViewById(R.id.enterTextNEF);
        mEditTextTitle = (EditText) view.findViewById(R.id.titleNEF);
        mImageView = (ImageView) view.findViewById(R.id.imageViewNEF);
        if (mNote != null) {
            mEditTextTitle.setText(mNote.getmTitle());
            mEditTextEnter.setText(mNote.getDescriptionTV());
        }
    }

    private void greateObject() {
        mRealm.beginTransaction();
        int key;
        try {
            key = mRealm.where(Note.class).findAll().size() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }

        Note note = new Note();
        note = getDataNote(note);
        note.setId(key);
        mRealm.copyToRealmOrUpdate(note);
        mRealm.commitTransaction();
        hideKeyboard(getActivity(), getView());
        mNoteActivity.onBackPressed();
        mRealm.close();
    }

    private void editObject() {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(getDataNote(mNote));
        mRealm.commitTransaction();
        hideKeyboard(getActivity(), getView());
        mNoteActivity.onBackPressed();
    }

    private Note getDataNote(Note note) {
        note.setmTitle(mEditTextTitle.getText().toString());
        note.setDescriptionTV(mEditTextEnter.getText().toString());
        note.setmDate(new Date(System.currentTimeMillis()));
        return note;
    }
}
