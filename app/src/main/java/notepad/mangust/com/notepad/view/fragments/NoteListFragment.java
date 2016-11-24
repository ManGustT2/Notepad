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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseFragment;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.activities.NoteActivity;
import notepad.mangust.com.notepad.view.adapters.NoteRecycleViewAdapter;
import notepad.mangust.com.notepad.view.dialog.RemoveDialogFragment;

/**
 * Created by Администратор on 26.09.2016.
 */
public class NoteListFragment extends BaseFragment {
    private static final int REQUEST_REMOVE = 1;

    private NoteActivity mNoteActivity;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private NoteRecycleViewAdapter mNoteRecycleViewAdapter;
    private Realm realm;

    private ItemClickListener<Note> mItemClickListener = new ItemClickListener<Note>() {
        @Override
        public void onItemLongClicked(Note item) {
            openRemovePicker(item.getId());
        }

        @Override
        public void onItemClick(Note item) {
            mNoteActivity.repleiceFragment(NoteDetailFragment.newInstance(item), true);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoteActivity = (NoteActivity) context;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteActivity.setTitle("Notepad");
//        mNoteActivity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
//        mNoteActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_note, container, false);
        findUI(v);
        initListeners();
        mNoteRecycleViewAdapter.apply(realm.where(Note.class).findAll());
        return v;
    }

    private void findUI(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabNLF);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoteRecycleViewAdapter = new NoteRecycleViewAdapter(mItemClickListener);
        mRecyclerView.setAdapter(mNoteRecycleViewAdapter);
    }

    private void initListeners() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoteActivity.repleiceFragment(new NoteEnterFragment(), true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteActivity.setTitle("Notepad");
        mNoteActivity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        mNoteActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if(mNoteRecycleViewAdapter != null) {
            mNoteRecycleViewAdapter.update(realm.where(Note.class).findAll());
        }

    }

    private void openRemovePicker(int noteId) {
        DialogFragment fragment = new RemoveDialogFragment();
        Bundle args = new Bundle();
        args.putInt("tag", noteId);
        fragment.setArguments(args);
        fragment.setTargetFragment(this, REQUEST_REMOVE);
        fragment.show(getFragmentManager(), fragment.getClass().getName());

    }

    // TODO: 11.11.2016 передать позицию через интент  и через saveState вывести в LOG;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_REMOVE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    int nodeId = data.getIntExtra(RemoveDialogFragment.EXTRA_NODE_ID, -1);
                    if (nodeId >= 0) {
                        realm.beginTransaction();
                        Note results = realm.where(Note.class).equalTo("id", nodeId).findFirst();
                        results.deleteFromRealm();
                        realm.commitTransaction();
                        mNoteRecycleViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("DeleteDialogResult", "Invalid item position");
                    }
                } else {
                    Log.e("DeleteDialogResult", "Data is null");
                }
            }
        }
    }
}

