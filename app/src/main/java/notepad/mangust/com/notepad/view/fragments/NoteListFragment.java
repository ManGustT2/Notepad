package notepad.mangust.com.notepad.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.model.OnItemClick;
import notepad.mangust.com.notepad.view.activities.NoteActivity;
import notepad.mangust.com.notepad.view.adapters.NoteRecycleViewAdapter;
import notepad.mangust.com.notepad.view.dialog.RemoveDialogFragment;

/**
 * Created by Администратор on 26.09.2016.
 */
public class NoteListFragment extends BaseFragment{
    private static final int REQUEST_REMOVE = 1;

    private NoteActivity mNoteActivity;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private List<Note> list = new ArrayList();
    private NoteRecycleViewAdapter mNoteRecycleViewAdapter;
    private int mPositions;
    private LinearLayoutManager mLinearLayoutManager;
    private Realm realm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoteActivity = (NoteActivity)context;
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(context).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_note, container, false);
        list = realm.where(Note.class).findAll();
        findUI(v);
        initListeners();
        if(list != null)
            mNoteRecycleViewAdapter.update(list);
        return v;
    }

    private void findUI(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabNLF);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNoteRecycleViewAdapter = new NoteRecycleViewAdapter(list, getActivity());
        mRecyclerView.setAdapter(mNoteRecycleViewAdapter);
        mNoteRecycleViewAdapter.setItemListener(onItemClick);
        mNoteRecycleViewAdapter.setOnLongClickListener(onLongItemClick);
    }

    private void initListeners(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoteActivity.repleiceFragment(new NoteEnterFragment(), true);
            }
        });
    }

    @Override
    public void onResume() {
        mNoteActivity.setTitle("Notepad");
       // mNoteActivity.showDoneIcon(false);
        if(mNoteRecycleViewAdapter != null){
            list = realm.where(Note.class).findAll();
            mNoteRecycleViewAdapter.update(list);
        }

        super.onResume();
    }

    private OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onItemClick(int position) {
            Note note = list.get(position);
            mNoteActivity.repleiceFragment(NoteDetailFragment.newInstance(note), true);
        }
    };

    private OnLongItemClick onLongItemClick = new OnLongItemClick() {
        @Override
        public void onItemLongClicked(int position) {
            mPositions = position;
            openRemovePicker();
            }

    };

     private void openRemovePicker() {
        DialogFragment fragment = new RemoveDialogFragment();
        fragment.setTargetFragment(this, REQUEST_REMOVE);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            realm.beginTransaction();
            Note results = realm.where(Note.class).equalTo("id", list.get(mPositions).getId()).findFirst();
            results.deleteFromRealm();
            realm.commitTransaction();
            mNoteRecycleViewAdapter.update(list);
        }
    }
}
