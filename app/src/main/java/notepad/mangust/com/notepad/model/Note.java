package notepad.mangust.com.notepad.model;

import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Администратор on 26.09.2016.
 */
public class Note extends RealmObject implements Serializable {
    private String mTitle;
    private Date mDate;

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }
    @PrimaryKey
    private int id = 0;

    public String  getDescriptionTV() {
        return descriptionTV;
    }

    public void setDescriptionTV(String descriptionTV) {
        this.descriptionTV = descriptionTV;
    }

    private String descriptionTV;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

//    private String convertData(){
//        SimpleDateFormat formater = new SimpleDateFormat("EEEE.MMM.yyyy,hh:mm:ss");
//        return formater.format(mDate);
//    }
//
//    public String getDate(){
//        return convertData();
//    }
}
