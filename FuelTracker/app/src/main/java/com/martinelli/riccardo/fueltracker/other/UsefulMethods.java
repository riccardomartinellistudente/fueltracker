package com.martinelli.riccardo.fueltracker.other;

/**
 * Created by Riccardo on 16/04/2016.
 */
public final class UsefulMethods {

    private UsefulMethods(){}

    public static String getFormattedTime(long milliseconds) {
        int secs = (int) Math.round((double) milliseconds / 1000); // for millisecs arg instead of secs
        if (secs < 60)
            return secs + "s";
        else {
            long mins = (int) secs / 60;
            long remainderSecs = secs - (mins * 60);
            if (mins < 60) {
                return (mins < 10 ? "0" : "") + mins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
            else {
                long hours = (int) mins / 60;
                long remainderMins = mins - (hours * 60);
                return (hours < 10 ? "0" : "") + hours + "h "
                        + (remainderMins < 10 ? "0" : "") + remainderMins + "m "
                        + (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
            }
        }
    }
}
