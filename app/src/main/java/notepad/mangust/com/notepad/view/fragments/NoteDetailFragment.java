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
import notepad.mangust.com.notepad.model.OnItemClick;
import notepad.mangust.com.notepad.view.activities.NoteActivity;

/**
 * Created by Администратор on 26.09.2016.
 */
public class NoteDetailFragment extends BaseFragment {
    private NoteActivity activity;
    private TextView titleTV;
    private TextView descriptionTV;
    private FloatingActionButton fabDetaile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NoteActivity)context;

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
        descriptionTV = (TextView)view.findViewById(R.id.description_FDN);
        fabDetaile = (FloatingActionButton)view.findViewById(R.id.fabNDF);

    }

    private void initListeners(){
        fabDetaile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
