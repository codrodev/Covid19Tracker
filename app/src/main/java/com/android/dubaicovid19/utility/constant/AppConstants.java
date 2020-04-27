
package com.android.dubaicovid19.utility.constant;

import android.Manifest;

public final class AppConstants {

    public static final String MYPREFERENCES = "MyPref";
    public static final String SHARED_KEY = "sharedKey";
    public static final String USER_ID_KEY = "userID";
    public static final String MOBILE_KEY = "mobile";
    public static final String USER_NAME_KEY = "userName";
    public static final String EMAIL_KEY = "email";
    public static final String COVID_KEY = "covid";
    public static final String GENDER_KEY = "gender";
    public static final String ID_PROOF_KEY = "idProof";
    public static final String LAT_KEY = "lat";
    public static final String LONG_KEY = "lon";
    public static final String UUID_KEY = "unique";
    public static final String INFECTION_KEY = "isInfection";
    public static final String USER_LANGUAGE = "language";

    public static String CURRENT_LOCALE = "en";

    public static String BASE_URL = "http://access.spaceimagingme.com:6092/Covid19Tracker/api/covid/";
    public static String REGISTER_USER = BASE_URL + "registerUser";
    public static String USER_MOVEMENT_TRACE = BASE_URL + "UserMovementTrace";
    public static String GET_COVID_CENTER = BASE_URL + "GetCovidCenter";
    public static String GET_GOVT_ANNOUNCEMENT = BASE_URL + "GetGovtAnnouncement";
    public static String POST_SELF_ASSESSMENT = BASE_URL + "Recommendation";
    public static String NEAR_BY_COVID = BASE_URL + "NearByCovidRegisteredDevice";
    public static String GET_NEAR_BY_COVID_CENTER = BASE_URL + "NearByCovidCenter";
    public static String GET_LIVE_DATA = BASE_URL + "GetLiveStatistics?StatisticsOf=";
    public static String GET_CHART_DATA = BASE_URL + "GetDataForCharts?Region=";
    public static String GET_NEAR_BY_INFECTED_USER = BASE_URL + "NeighbourhoodInfectedUserCount";
    public static String NearByDevicesByLoc = BASE_URL + "NearByDevicesByLoc";
    public static String GET_COVID_TEST_STATS = BASE_URL + "GetTestStatistics";
    public static String UPDATE_COVID_LOG = BASE_URL + "updatecovidlog";
    public static String GET_CONTAINMENT_CLUSTER = BASE_URL + "GetContainmentZones";
    public static String NEAR_BY_CONTAINMENT_CLUSTER = BASE_URL + "NearByContainmentZones";

    public static String[] ALL_PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
