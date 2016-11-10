package notepad.mangust.com.notepad.view.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.Date;
import io.realm.Realm;
import io.realm.internal.IOException;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

public class  NoteEnterFragment extends BaseFragment {
    private NoteActivity mNoteActivity;
    private EditText mEditTextTitle;
    private EditText mEditTextEnter;
    private ImageView mImageView;
    private Note mNote;
    private Realm mRealm;
    static final int GALLERY_REQUEST = 1;

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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        switch (requestCode){
            case GALLERY_REQUEST:
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap( , selectedImage);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                mImageView.setImageBitmap(bitmap);
        }
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

    private void findUI(View view){
        mNoteActivity.setTitle("New Note");
        mEditTextEnter = (EditText) view.findViewById(R.id.enterTextNEF);
        mEditTextTitle = (EditText) view.findViewById(R.id.titleNEF);
        mImageView = (ImageView) view.findViewById(R.id.imageViewNEF);
        if (mNote != null) {
            mEditTextTitle.setText(mNote.getmTitle());
            mEditTextEnter.setText(mNote.getDescriptionTV());
        }
    }


    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void greateObject(){
        mRealm.beginTransaction();
        int key;
        try {
            key = mRealm.where(Note.class).findAll().size()+1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }

        mNote = mRealm.createObject(Note.class);
        mNote.setmTitle(mEditTextTitle.getText().toString());
        mNote.setId(key);
        mNote.setDescriptionTV(mEditTextEnter.getText().toString());
        mNote.setmDate(new Date(System.currentTimeMillis()));
        mRealm.commitTransaction();
        hideKeyboard(getActivity(),getView());
        mNoteActivity.onBackPressed();
    }

    private void editObject(){
        mRealm.beginTransaction();
        mNote.setmTitle(mEditTextTitle.getText().toString());
        mNote.setDescriptionTV(mEditTextEnter.getText().toString());
        mNote.setmDate(new Date(System.currentTimeMillis()));
        mNote = mRealm.copyToRealmOrUpdate(mNote);
        mRealm.commitTransaction();
        hideKeyboard(getActivity(), getView());
        mNoteActivity.onBackPressed();
    }
}
