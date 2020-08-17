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
    public static String GETALLBOOKS = PROTOCOL + HOSTNAME + "/getallbooks.php";
    public static String GETALLBOOKOFAUTHORS = PROTOCOL + HOSTNAME + "/getallbookofauthors.php";
    public static String GETALLUSERS = PROTOCOL + HOSTNAME + "/getlistuser.php";
    public static String EDITUSER = PROTOCOL + HOSTNAME + "/updateuser.php";
    public static String ADDCATEGORY = PROTOCOL + HOSTNAME + "/addcategory.php";
    public static String EDITCATEGORY = PROTOCOL + HOSTNAME + "/updatecategory.php";
    public static String GETRULE = PROTOCOL + HOSTNAME + "/getrule.php";
    public static String EDITRULE = PROTOCOL + HOSTNAME + "/updaterule.php";
    public static String CHANGEPASSWORD = PROTOCOL + HOSTNAME + "/changepassword.php";
    public static String GETBORROWEDBOOK = PROTOCOL + HOSTNAME + "/getborrowedbook.php";
    public static String DELETEUSER = PROTOCOL + HOSTNAME + "/deleteuser.php";
}
