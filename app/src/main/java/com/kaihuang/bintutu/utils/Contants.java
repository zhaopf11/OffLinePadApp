package com.kaihuang.bintutu.utils;


public class Contants {

    // 企业
    public static String siteid = "au_1000";// 示例kf_9979,kf_8002,kf_3004,zf_1000,yy_1000
    public static String sdkkey = "FEA65C0A-7517-439B-BBFA-B776F9772501";// 示例FB7677EF-00AC-169D-1CAD-DEDA35F9C07B

    public static final String TOKEN = "token";
    public static final String USER_ID = "user_id";

//    public static String BASE_URL = "http://192.168.0.210:8010/appAction/";

    public static String BASE_URL = "http://101.37.147.118:8010/appAction/";//服务器地址
    private static String action = "";

    public static final String UPLOAD_URL = "http://101.37.147.118:8010/uploadFile";
    public static final String upload_image = "http://apiqa.bintutu.com/KHAppUpload/httpserver";
    public static String contactSSID = "";
    public static String bindingUserID = "10943";
    public static String bindPhone = "15981820906";
    public static String relatePhone = "";
    public static String relateUserFootId = "";
    public static String ssid = "";

    public static class API {
        public static String sendCode = BASE_URL + "phoneCodeSend" + action;
        public static String phoneCodeLogin = BASE_URL + "phoneCodeLogin" + action;
        public static String phonePassLogin = BASE_URL + "checkPasswordLogin" + action;
        public static String myDetail = BASE_URL + "getSalesmanDetail" + action;
        public static String getMonthOrDayOrAllResults = BASE_URL + "getMonthOrDayOrAllResults" + action;
        public static String myTeamSalesmanList = BASE_URL + "myTeamSalesmanList" + action;
        public static String getResultsDetail = BASE_URL + "getResultsDetail" + action;
        public static String equipmentRepair = BASE_URL + "equipmentRepair" + action;
        public static String relationUser = BASE_URL + "relationUser" + action;
        public static String saveUserFootData = BASE_URL + "saveUserFootData" + action;
        public static String uploadInitialFootData = BASE_URL + "uploadInitialFootData" + action;
        public static String addRepair = BASE_URL + "addRepair";//报修接口

    }

    public static String machineIP = "http://192.168.100.1:80/";
}
