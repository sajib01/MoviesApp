package info.sajib.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajib on 18-03-2016.
 */
public class Push implements Parcelable{
    private long id;
    private String message;
    private String title;
    private String bigTitle;
    private String imageUrl;
    private String iconUrl;
    private int priority;
    public static final int PRIORITY_HIGH = 0;
    public Push(){

    }
    protected Push(Parcel in) {
        id = in.readLong();
        message = in.readString();
        title = in.readString();
        bigTitle = in.readString();
        imageUrl = in.readString();
        iconUrl = in.readString();
        priority = in.readInt();
    }

    public static final Creator<Push> CREATOR = new Creator<Push>() {
        @Override
        public Push createFromParcel(Parcel in) {
            return new Push(in);
        }

        @Override
        public Push[] newArray(int size) {
            return new Push[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(message);
        dest.writeString(title);
        dest.writeString(bigTitle);
        dest.writeString(imageUrl);
        dest.writeString(iconUrl);
        dest.writeInt(priority);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBigTitle() {
        return bigTitle;
    }

    public void setBigTitle(String bigTitle) {
        this.bigTitle = bigTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
