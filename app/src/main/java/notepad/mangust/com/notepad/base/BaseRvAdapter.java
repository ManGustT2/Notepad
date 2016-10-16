package notepad.mangust.com.notepad.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import notepad.mangust.com.notepad.R;

/**
 * Created by Администратор on 27.09.2016.
 */
public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<BaseRvAdapter.ViewHolder> {
    private List<T> list;

    public BaseRvAdapter(List<T> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.title_note, parent, false);
        return new ViewHolder(v);
    }

    public void setList(List<T> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<T> getList(){
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llItemContainer);
        }
    }
}
