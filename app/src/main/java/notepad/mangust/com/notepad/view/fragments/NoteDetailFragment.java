package notepad.mangust.com.notepad.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

/**
 * Created by Администратор on 26.09.2016.
 */
public class NoteDetailFragment extends BaseFragment {
    private static final String TAG = "NoteDetailFragment";
    private NoteActivity activity;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private Note mNote;
    public static String ENTER_KEY = "enterkey";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NoteActivity)context;;
    }

    public static NoteDetailFragment newInstance(Note note){
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle b = new Bundle();
        b.putParcelable(TAG, note);
        fragment.setArguments(b);
        return fragment;
    }

    private void getArgs(){
        mNote = getArguments().getParcelable(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_note, container, false);
        getArgs();
        activity.setTitle(mNote.getmTitle());
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findUI(v);
        return v;
    }

    private void findUI(View view){
        mTitleTextView = (TextView)view.findViewById(R.id.title_FDN);
        mTitleTextView.setText(mNote.getmTitle());
        mDescriptionTextView = (TextView)view.findViewById(R.id.description_FDN);
        mDescriptionTextView.setText(mNote.getDescriptionTV());
        FloatingActionButton mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fabNDF);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEnterFragment noteEnterFragment = new NoteEnterFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(ENTER_KEY, mNote);
                noteEnterFragment.setArguments(bundle);
                activity.repleiceFragment(noteEnterFragment, true);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
