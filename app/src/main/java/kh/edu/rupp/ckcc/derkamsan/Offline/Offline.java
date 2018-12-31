package kh.edu.rupp.ckcc.derkamsan.Offline;

public class Offline {

    private String imageUrl;
    private String place;
    private int sizeOfData;

    public Offline(String imageUrl, String place, int sizeOfData) {
        this.imageUrl = imageUrl;
        this.place = place;
        this.sizeOfData = sizeOfData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getSizeOfData() {
        return sizeOfData;
    }

    public void setSizeOfData(int sizeOfData) {
        this.sizeOfData = sizeOfData;
    }
}
