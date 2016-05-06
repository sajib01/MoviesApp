package info.sajib.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajib on 11-03-2016.
 */
public class Casts implements Parcelable{
    private String cast;

    private String chracter;

    private String charaterimage;

    private int id;

    public Casts(){

    }
    public Casts(Parcel in) {
        cast = in.readString();
        chracter = in.readString();
        charaterimage = in.readString();
        id = in.readInt();
    }

    public static final Creator<Casts> CREATOR = new Creator<Casts>() {
        @Override
        public Casts createFromParcel(Parcel in) {
            return new Casts(in);
        }

        @Override
        public Casts[] newArray(int size) {
            return new Casts[size];
        }
    };

    public int getId(){
        return id;
    }

    public void setId(int id)
    {
        this.id=id;
    }

    public String getCast()
    {
        return cast;
    }

    public void setCast(String cast)
    {
        this.cast=cast;
    }

    public String getCharaterimage()
    {
        return charaterimage;
    }

    public void setCharaterimage(String charaterimage)
    {
        this.charaterimage=charaterimage;
    }
    public String getChracter()
    {
        return chracter;
    }

    public void setChracter(String chracter)
    {
        this.chracter=chracter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cast);
        dest.writeString(chracter);
        dest.writeString(charaterimage);
        dest.writeInt(id);
    }
}
