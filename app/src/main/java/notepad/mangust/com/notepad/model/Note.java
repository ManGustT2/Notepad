package notepad.mangust.com.notepad.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Администратор on 26.09.2016.
 */
public class Note extends RealmObject implements Parcelable{
    private int id;
    private String mTitle;
    private Date mDate;
    private String descriptionTV;

    public Note(){

    }

    protected Note(Parcel in) {
        id = in.readInt();
        mTitle = in.readString();
        descriptionTV = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDescriptionTV() {
        return descriptionTV;
    }

    public void setDescriptionTV(String descriptionTV) {
        this.descriptionTV = descriptionTV;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mTitle);
        dest.writeString(descriptionTV);
    }
}
