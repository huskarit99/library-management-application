package com.example.librarymanagement.networks;

public class Server {
    public static String localhost = "library-management-application.000webhostapp.com";

    public static String addUser = "http://" + localhost + "/adduser.php";

    public static String getUser = "https://" + localhost + "/getuser.php";
    public static String getInfoUser = "https://" + localhost + "/getinfouser.php";
}
