package com.dongyang.yeonhwaproject.Common;

/**
 * Created by JongHwa on 2018-06-07.
 */

public class GlobalInfo {

    public final static String findHosPharKey = "qykcrKr3huKnjZV66xsvPHACE4seVKMSy6yWtpXPqvEBBWLFqYkzcm7bNXfDdrYs0pZ9uWh%2BlHKwh6pzSNw9Mw%3D%3D";
    public final static String findHosKey = "qykcrKr3huKnjZV66xsvPHACE4seVKMSy6yWtpXPqvEBBWLFqYkzcm7bNXfDdrYs0pZ9uWh%2BlHKwh6pzSNw9Mw%3D%3D";
    public final static String findPharKey = "qykcrKr3huKnjZV66xsvPHACE4seVKMSy6yWtpXPqvEBBWLFqYkzcm7bNXfDdrYs0pZ9uWh%2BlHKwh6pzSNw9Mw%3D%3D";
    public final static String findHosURL = " http://apis.data.go.kr/B551182/hospInfoService/getHospBasisList";
    public final static String findPharURL = "http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList";
    public final static String findHosPharURL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire";

    public static boolean isSettingLocation = false;
    public static double settingLatitude, settingLongitude;

    public static final String SERVER_URL = "http://52.79.37.133:10330/";

    public static boolean isLogin = false;
    public static String user_id;
    public static String user_name;
    public static String user_phone;
    public static String user_email;
    public static String user_image;
}
