package notepad.mangust.com.notepad.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import notepad.mangust.com.notepad.R;
import notepad.mangust.com.notepad.base.BaseActivity;
import notepad.mangust.com.notepad.view.fragments.NoteListFragment;

public class NoteActivity extends BaseActivity {
    private static final String TAG = "TAG";
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;
    private TextView mTextView;
    private ImageView mImegeViewDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTextView = (TextView) mToolbar.findViewById(R.id.tvTitle);
        mImegeViewDone = (ImageView) mToolbar.findViewById(R.id.ivSave);
        setSupportActionBar(mToolbar);
        mFragmentManager = getSupportFragmentManager();

        Fragment fragment = mFragmentManager.findFragmentById(R.id.container);

        if (fragment == null)
            repleiceFragment(new NoteListFragment(), false);

    }

    public void addFragment(Fragment fragment){
        mFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    public void repleiceFragment(Fragment fragment, boolean addBackStack){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, TAG);
        if(addBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void setTitle(String title){
        mTextView.setText(title);
    }

    public void showDoneIcon(boolean isVisible){
        if(isVisible){
            mImegeViewDone.setVisibility(View.VISIBLE);
        } else {
            mImegeViewDone.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
    }
}
