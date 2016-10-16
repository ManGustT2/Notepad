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

/**
 * Created by Администратор on 28.09.2016.
 */
public class NoteEnterFragment extends BaseFragment {
    private NoteActivity activity;
    private EditText titleET;
    private EditText enterET;
    private Note note;
    private ImageView mIvSave;
    private Realm realm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity =(NoteActivity)context;
        if (getArguments() != null)
        note = (Note)getArguments().getSerializable(NoteDetailFragment.ENTER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_enter_note, container, false);

        realm = Realm.getDefaultInstance();

        findUI(v);

        return v;
    }

    private void findUI(View view){
        mIvSave = (ImageView) activity.getToolbar().findViewById(R.id.ivSave);
        activity.showDoneIcon(true);
        activity.setTitle("New note");
        enterET = (EditText) view.findViewById(R.id.enterTextNEF);
        titleET = (EditText) view.findViewById(R.id.titleNEF);
        if (note != null) {
            titleET.setText(note.getmTitle());
            enterET.setText(note.getDescriptionTV());
        }

        mIvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterET.getText().toString().trim().length() > 0
                        && titleET.getText().toString().trim().length() > 0) {
                    if (note == null) {
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
        realm.beginTransaction();
        int key;
        try {
            key = realm.where(Note.class).findAll().size()+1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }

        note = realm.createObject(Note.class);
        note.setmTitle(titleET.getText().toString());
        note.setId(key);
        note.setDescriptionTV(enterET.getText().toString());
        note.setmDate(new Date(System.currentTimeMillis()));
        realm.commitTransaction();
        hideKeyboard(getActivity(), mIvSave);
        activity.onBackPressed();
    }

    private void editObject(){
        realm.beginTransaction();
        note.setmTitle(titleET.getText().toString());
        note.setDescriptionTV(enterET.getText().toString());
        note.setmDate(new Date(System.currentTimeMillis()));
        note = realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
        hideKeyboard(getActivity(), mIvSave);
        activity.onBackPressed();
    }

}
