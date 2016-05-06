package info.sajib.moviesapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sajib on 12-03-2016.
 */
public class PersonDetails implements Parcelable{
    private int id;
    private String name;
    private String description;
    private String birthdate;
    private String placeofbirth;
    private String profilepic;
    private String slideimage;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPlaceofbirth(String placeofbirth) {
        this.placeofbirth = placeofbirth;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public void setSlideimage(String slideimage) {
        this.slideimage = slideimage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getBirthdate() {
        return birthdate;
    }

    public String getPlaceofbirth() {
        return placeofbirth;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getSlideimage() {
        return slideimage;
    }

    public PersonDetails()
    {

    }
    PersonDetails(Parcel in)
    {

        id = in.readInt();
        name = in.readString();
        description = in.readString();
        birthdate = in.readString();
        placeofbirth = in.readString();
        profilepic = in.readString();
        slideimage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(birthdate);
        dest.writeString(placeofbirth);
        dest.writeString(profilepic);
        dest.writeString(slideimage);
    }

    public static final Creator<PersonDetails> CREATOR = new Creator<PersonDetails>() {
        @Override
        public PersonDetails createFromParcel(Parcel in) {
            return new PersonDetails(in);
        }

        @Override
        public PersonDetails[] newArray(int size) {
            return new PersonDetails[size];
        }
    };

}
