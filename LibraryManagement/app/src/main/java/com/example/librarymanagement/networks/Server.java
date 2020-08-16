package com.example.librarymanagement.networks;

public class Server {
    public static String localhost = "library-management-application.000webhostapp.com";

    public static String addUser = "http://" + localhost + "/adduser.php";

    public static String getUser = "http://" + localhost + "/getuser.php";

    public static String getListUser = "http://" + localhost + "/getlistuser.php";

    public static String editUser = "http://" + localhost + "/updateuser.php";

    public static String getListCategory = "http://" + localhost + "/getallcategories.php";

    public static String addCategory = "http://" + localhost + "/addcategory.php";

    public static String editCategory = "http://" + localhost + "/updatecategory.php";

    public static String getRule = "http://" + localhost + "/getrule.php";

    public static String editRule = "http://" + localhost + "/updaterule.php";

    public static String changePassword = "http://" + localhost + "/changepassword.php";
}
