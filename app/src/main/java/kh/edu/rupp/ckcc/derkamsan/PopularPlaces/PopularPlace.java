package kh.edu.rupp.ckcc.derkamsan.PopularPlaces;

import com.google.firebase.firestore.GeoPoint;
import java.util.List;

public class PopularPlace {
    private String id;
    private List<String> imageUrl;
    private GeoPoint location;
    private String place;
    private String Description;

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
