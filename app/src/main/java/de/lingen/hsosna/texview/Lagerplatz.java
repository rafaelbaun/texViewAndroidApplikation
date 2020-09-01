package de.lingen.hsosna.texview;

import android.os.Parcel;
import android.os.Parcelable;

public class Lagerplatz implements Parcelable {
    private int lagerort;
    private int lagerplatz;
    private int regalfach;

    public Lagerplatz (int lagerort, int lagerplatz) {
        this.lagerort = lagerort;
        this.lagerplatz = formatLagerplatz(lagerplatz);
        this.regalfach = getRegalfachFromLagerplatz(lagerplatz);
    }

    public Lagerplatz (int lagerort, int lagerplatz, int regalfach) {
        this.lagerort = lagerort;
        this.lagerplatz = lagerplatz;
        this.regalfach = regalfach;
    }

    protected Lagerplatz (Parcel in) {
        int[] data = new int[3];

        in.readIntArray(data);

        lagerort = data[0];
        lagerplatz = data[1];
        regalfach = data[2];
    }

    public static final Creator<Lagerplatz> CREATOR = new Creator<Lagerplatz>() {
        @Override
        public Lagerplatz createFromParcel (Parcel in) {
            return new Lagerplatz(in);
        }

        @Override
        public Lagerplatz[] newArray (int size) {
            return new Lagerplatz[size];
        }
    };

    private int getRegalfachFromLagerplatz (int lagerplatz) {
        String lagerplatzString = String.valueOf(lagerplatz);
        String finalString = "" + lagerplatzString.charAt(3);
        return Integer.parseInt(finalString);
    }

    private int formatLagerplatz (int lagerplatz) {
        String lagerplatzString = String.valueOf(lagerplatz);
        String finalString = lagerplatzString.substring(0,2) + "0" + lagerplatzString.charAt(2);

        return Integer.parseInt(finalString);
    }

    public int getLagerort () {
        return lagerort;
    }

    public void setLagerort (int lagerort) {
        this.lagerort = lagerort;
    }

    public int getLagerplatz () {
        return lagerplatz;
    }

    public void setLagerplatz (int lagerplatz) {
        this.lagerplatz = lagerplatz;
    }

    public int getRegalfach () {
        return regalfach;
    }

    public void setRegalfach (int regalfach) {
        this.regalfach = regalfach;
    }

    public CharSequence getLocation(){
        return "60" + String.valueOf(getLagerplatz());
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(lagerort);
        dest.writeInt(lagerplatz);
        dest.writeInt(regalfach);
    }
}
