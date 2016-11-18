package notepad.mangust.com.notepad.view.fragments;


public interface ItemClickListener<T> {
    void onItemLongClicked(T item);

    void onItemClick(T item);
}