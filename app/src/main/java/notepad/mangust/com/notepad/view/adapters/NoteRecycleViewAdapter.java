package notepad.mangust.com.notepad.view.adapters;

import android.content.Context;
import android.view.View;
import java.util.List;
import notepad.mangust.com.notepad.base.BaseRecyclerViewAdapter;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.model.OnItemClick;
import notepad.mangust.com.notepad.view.fragments.OnLongItemClick;

/**
 * Created by Администратор on 27.09.2016.
 */
public class NoteRecycleViewAdapter extends BaseRecyclerViewAdapter<Note> {
    private Context mContext;
    private OnItemClick mOnItemClick;
    private OnLongItemClick mOnLongItemClick;

    public void setItemListener(OnItemClick itemListener){
        mOnItemClick = itemListener;
    }

    public void setOnLongClickListener(OnLongItemClick longItemClick){
        mOnLongItemClick = longItemClick;
    }

    public NoteRecycleViewAdapter(List<Note> list, Context c) {
        super(list);
        mContext = c;
    }

    public void update(List<Note> items){
        setList(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.tvDate.setText(getList().get(position).getmDate().toString());
        holder.tvTitle.setText(getList().get(position).getmTitle());
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(position);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnLongItemClick.onItemLongClicked(position);
                return true;
            }
        });
    }
}

