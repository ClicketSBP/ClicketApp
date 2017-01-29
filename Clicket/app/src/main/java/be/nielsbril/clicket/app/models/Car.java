package be.nielsbril.clicket.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {

    private int _id;
    private int user_id;
    private String name;
    private String license_plate;
    private boolean default_car;

    public Car(int id, int user_id, String name, String license_plate, boolean default_car) {
        this._id = id;
        this.user_id = user_id;
        this.name = name;
        this.license_plate = license_plate;
        this.default_car = default_car;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public boolean isDefault_car() {
        return default_car;
    }

    public void setDefault_car(boolean default_car) {
        this.default_car = default_car;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.get_id());
        parcel.writeInt(this.getUser_id());
        parcel.writeString(this.getName());
        parcel.writeString(this.getLicense_plate());
        parcel.writeByte((byte) (this.isDefault_car() ? 1 : 0));
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    protected Car(Parcel in) {
        set_id(in.readInt());
        setUser_id(in.readInt());
        setName(in.readString());
        setLicense_plate(in.readString());
        setDefault_car(in.readByte() != 0);
    }

}