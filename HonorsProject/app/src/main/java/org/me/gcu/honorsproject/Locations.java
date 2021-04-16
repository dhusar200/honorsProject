package org.me.gcu.honorsproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Locations implements Parcelable {

    private String name;
    private String continent;
    private String country;
    private String state;
    private String city;
    private String postCode;
    private String streetName;
    private String number;
    private float glat;
    private float glong;
    private int maxCapacity;

    public Locations() {
        this.name = "test";
        this.continent = "test";
        this.country = "test";
        this.state = "test";
        this.city = "test";
        this.postCode = "test";
        this.streetName = "test";
        this.number = "test";
        this.maxCapacity = 50;
    }

    public Locations(String name, String continent, String country, String state, String city, String postCode, String streetName, String number, int maxCapacity) {
        this.name = name;
        this.continent = continent;
        this.country = country;
        this.state = state;
        this.city = city;
        this.postCode = postCode;
        this.streetName = streetName;
        this.number = number;
        this.maxCapacity = maxCapacity;
    }

    protected Locations(Parcel in) {
        name = in.readString();
        continent = in.readString();
        country = in.readString();
        state = in.readString();
        city = in.readString();
        postCode = in.readString();
        streetName = in.readString();
        number = in.readString();
        glat = in.readFloat();
        glong = in.readFloat();
        maxCapacity = in.readInt();
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getGlat() {
        return glat;
    }

    public void setGlat(float glat) {
        this.glat = glat;
    }

    public float getGlong() {
        return glong;
    }

    public void setGlong(float glong) {
        this.glong = glong;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(continent);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(postCode);
        dest.writeString(streetName);
        dest.writeString(number);
        dest.writeFloat(glat);
        dest.writeFloat(glong);
        dest.writeInt(maxCapacity);
    }

    @Override
    public String toString() {
        return "Locations{" +
                "name='" + name + '\'' +
                ", continent='" + continent + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", postCode='" + postCode + '\'' +
                ", streetName='" + streetName + '\'' +
                ", number='" + number + '\'' +
                ", glat=" + glat +
                ", glong=" + glong +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
