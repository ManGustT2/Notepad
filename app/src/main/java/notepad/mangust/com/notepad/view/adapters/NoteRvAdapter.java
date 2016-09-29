package notepad.mangust.com.notepad.view.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import notepad.mangust.com.notepad.base.BaseRvAdapter;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.model.OnItemClick;
import notepad.mangust.com.notepad.view.fragments.NoteListFragment;

/**
 * Created by Администратор on 27.09.2016.
 */
public class NoteRvAdapter extends BaseRvAdapter<Note> {
    private List<Note> mItemList = new ArrayList<>();
    private Context context;
    private OnItemClick onItemClick;

    public void setItemListener(OnItemClick itemListener){
        onItemClick = itemListener;
    }


    public NoteRvAdapter(List<Note> list, Context c) {
        super(list);
        context = c;
    }

    public void update(List<Note> items){
        mItemList = items;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(BaseRvAdapter.ViewHolder holder, final int position) {
        holder.tvDate.setText(getList().get(position).getDate());
        holder.tvTitle.setText(getList().get(position).getmTitle());
        holder.tvTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(position);
            }
        });
    }
}

