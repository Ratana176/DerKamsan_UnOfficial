package kh.edu.rupp.ckcc.derkamsan;

import kh.edu.rupp.ckcc.derkamsan.Events.Event;
import kh.edu.rupp.ckcc.derkamsan.Homes.Home;
import kh.edu.rupp.ckcc.derkamsan.PopularPlaces.PopularPlace;
import kh.edu.rupp.ckcc.derkamsan.Profiles.Profile;

public class Singleton {

    private static Singleton INSTANCE;

    private Event[] events;
    private Home[] homes;
    private PopularPlace[] popularPlaces;
    private Profile profile;

    private Singleton() {
    }

    public Profile getProfile() {
        return profile;
    }

    public static Singleton getInstance() {
        if(INSTANCE == null){
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
    public Event[] getEvents() {
        return events;
    }

    public Home[] getHomes() {
        return homes;
    }
    public void setHomes(Home[] homes) {
        this.homes = homes;
    }

    public PopularPlace[] getPopularPlaces() {
        return popularPlaces;
    }

    public void setPopularPlaces(PopularPlace[] popularPlaces) {
        this.popularPlaces = popularPlaces;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
