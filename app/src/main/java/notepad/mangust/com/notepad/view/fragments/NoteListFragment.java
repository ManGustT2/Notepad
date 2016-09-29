package notepad.mangust.com.notepad.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.base.BaseRvAdapter;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.model.OnItemClick;
import notepad.mangust.com.notepad.view.activities.NoteActivity;
import notepad.mangust.com.notepad.view.adapters.NoteRvAdapter;

/**
 * Created by Администратор on 26.09.2016.
 */
public class NoteListFragment extends BaseFragment{
    private NoteActivity activity;
    private RecyclerView recyclerView;
    private FloatingActionButton fablist;
    private List<Note> list = new ArrayList();
    private NoteRvAdapter adapter;

    private Note note;
    private static NoteListFragment nFragment;
    private LinearLayoutManager llm;
    private FragmentManager fm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NoteActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_note, container, false);

        findUI(v);
        initListeners();
        prepareDate();

        return v;
    }

    private void findUI(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        fablist = (FloatingActionButton) view.findViewById(R.id.fabNLF);

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        adapter = new NoteRvAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(onItemClick);
    }

    private void initListeners(){
        fablist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.repleiceFragment(new NoteEnterFragment());
            }
        });
    }

    private void prepareDate(){
        for (int i = 0; i<10; i ++ ){
            Note note = new Note();
            note.setmDate(new Date(System.currentTimeMillis()));
            note.setmTitle("title " + i);
            list.add(note);
        }
        adapter.update(list);
    }
    private OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onItemClick(int position) {
            activity.repleiceFragment(new NoteDetailFragment());
        }
    };
}
