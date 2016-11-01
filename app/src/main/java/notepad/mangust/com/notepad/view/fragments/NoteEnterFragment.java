package notepad.mangust.com.notepad.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.Date;
import io.realm.Realm;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

public class NoteEnterFragment extends BaseFragment {
    private NoteActivity mNoteActivity;
    private EditText mEditTextTitle;
    private EditText mEditTextEnter;
    private Note mNote;
    private ImageView mIvSave;
    private Realm mRealm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoteActivity =(NoteActivity)context;
        if (getArguments() != null)
        mNote = (Note)getArguments().getParcelable(NoteDetailFragment.ENTER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_note, container, false);
        mRealm = Realm.getDefaultInstance();
        findUI(v);
        return v;
    }

    private void findUI(View view){
        mIvSave = (ImageView) mNoteActivity.getToolbar().findViewById(R.id.ivSave);
        mNoteActivity.showDoneIcon(true);
        mNoteActivity.setTitle("New mNote");
        mEditTextEnter = (EditText) view.findViewById(R.id.enterTextNEF);
        mEditTextTitle = (EditText) view.findViewById(R.id.titleNEF);
        if (mNote != null) {
            mEditTextTitle.setText(mNote.getmTitle());
            mEditTextEnter.setText(mNote.getDescriptionTV());
        }

        mIvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextEnter.getText().toString().trim().length() > 0
                        && mEditTextTitle.getText().toString().trim().length() > 0) {
                    if (mNote == null) {
                        greateObject();
                    } else {
                        editObject();
                    }
                }
            }
        });
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
        hideKeyboard(getActivity(), mIvSave);
        mNoteActivity.onBackPressed();
    }

    private void editObject(){
        mRealm.beginTransaction();
        mNote.setmTitle(mEditTextTitle.getText().toString());
        mNote.setDescriptionTV(mEditTextEnter.getText().toString());
        mNote.setmDate(new Date(System.currentTimeMillis()));
        mNote = mRealm.copyToRealmOrUpdate(mNote);
        mRealm.commitTransaction();
        hideKeyboard(getActivity(), mIvSave);
        mNoteActivity.onBackPressed();
    }
}
