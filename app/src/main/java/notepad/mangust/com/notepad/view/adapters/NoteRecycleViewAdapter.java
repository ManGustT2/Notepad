package notepad.mangust.com.notepad.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseRecyclerViewAdapter;
import notepad.mangust.com.notepad.model.Note;
import notepad.mangust.com.notepad.view.fragments.ItemClickListener;

/**
 * Created by Администратор on 27.09.2016.
 */
public class NoteRecycleViewAdapter extends BaseRecyclerViewAdapter<Note, NoteRecycleViewAdapter.ViewHolder> {
    private ItemClickListener<Note> mItemClickListener;

    public NoteRecycleViewAdapter(ItemClickListener<Note> itemClickListener) {
        super();
        mItemClickListener = itemClickListener;
    }

    @Override
    public NoteRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.title_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteRecycleViewAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void update(List<Note> items) {
        setList(items);
        notifyDataSetChanged();
    }

    private Note getItem(int position) {
        return getList().get(position);
    }

    @Override
    public void apply(List<Note> all) {
        setList(all);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llItemContainer);
        }

        void bind(final Note note) {
            tvDate.setText(note.getmDate().toString());
            tvTitle.setText(note.getmTitle());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(note);
                }
            });
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemClickListener.onItemLongClicked(note);
                    return true;
                }
            });
        }
    }
}

