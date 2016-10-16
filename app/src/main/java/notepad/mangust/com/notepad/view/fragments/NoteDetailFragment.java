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
    private NoteActivity activity;
    private TextView titleTV;
    private TextView descriptionTV;
    private FloatingActionButton fabDetaile;
    public static String ENTER_KEY = "enterkey";
    private Note note;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NoteActivity)context;
        note = (Note)getArguments().getSerializable(NoteListFragment.DETAIL_KEY);
        activity.setTitle(note.getmTitle());
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail_note, container, false);

        findUI(v);

        return v;
    }

    private void findUI(View view){
        titleTV = (TextView)view.findViewById(R.id.title_FDN);
        titleTV.setText(note.getmTitle());
        descriptionTV = (TextView)view.findViewById(R.id.description_FDN);
        descriptionTV.setText(note.getDescriptionTV());

        fabDetaile = (FloatingActionButton)view.findViewById(R.id.fabNDF);

        fabDetaile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEnterFragment noteEnterFragment = new NoteEnterFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ENTER_KEY, note);
                noteEnterFragment.setArguments(bundle);
                activity.repleiceFragment(noteEnterFragment, true);
            }
        });

    }

    @Override
    public void onResume() {
        activity.showDoneIcon(false);
        super.onResume();
    }
}
