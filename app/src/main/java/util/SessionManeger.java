package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.rouletteapplication.MainActivity;
import java.util.HashMap;

public class SessionManeger
{
    public static SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    // Sharedpref file name
    private static final String PREF_NAME = "ShopinationUser";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "userid";

    public static final String KEY_PHONE = "phoneno";

    public static final String KEY_PHOTO = "userFile";

    public static final String MEMBER_ID = "member_id";

    public SessionManeger(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=preferences.edit();
        // open SQLite when login user.
    }

    public  void createSession(String userId,String member_id, String email, String phone,String photo) {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID, userId);
        editor.putString(MEMBER_ID,member_id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PHOTO,photo);
        editor.commit();
        //  getWishListVolley(userId);
    }

    public boolean checkLogin() {
        if (!this.isLoggedIn())
        {
            Intent intent=new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String,String> getUserDetails() {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(KEY_ID,preferences.getString(KEY_ID,null));
        hashMap.put(KEY_NAME,preferences.getString(KEY_NAME,null));
        hashMap.put(KEY_EMAIL,preferences.getString(KEY_EMAIL,null));
        hashMap.put(KEY_PHONE,preferences.getString(KEY_PHONE,null));
        hashMap.put(MEMBER_ID,preferences.getString(MEMBER_ID,null));
        hashMap.put(KEY_PHOTO,preferences.getString(KEY_PHOTO,null));

        return hashMap;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public void remoteLogout() {
        editor.clear();
        editor.commit();
    }

}
