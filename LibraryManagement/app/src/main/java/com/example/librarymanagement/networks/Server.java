package com.example.librarymanagement.networks;

public class Server {
    public static String HOSTNAME = "library-management-application.000webhostapp.com";
    public static String PROTOCOL = "https://";

    public static String ADDUSER = PROTOCOL + HOSTNAME + "/adduser.php";
    public static String ADDBOOK = PROTOCOL + HOSTNAME + "/addbook.php";

    public static String GETUSER = PROTOCOL + HOSTNAME + "/getuser.php";
    public static String GETINFOUSER = PROTOCOL + HOSTNAME + "/getinfouser.php";
    public static String GETALLAUTHORS = PROTOCOL + HOSTNAME + "/getallauthors.php";
    public static String GETALLCATEGORIES = PROTOCOL + HOSTNAME + "/getallcategories.php";
    public static String GETALLPUBLISHERS = PROTOCOL + HOSTNAME + "/getallpublishers.php";
}