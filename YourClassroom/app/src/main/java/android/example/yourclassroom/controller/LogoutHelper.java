package android.example.yourclassroom.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import android.example.yourclassroom.controller.ComposeActivity;

public class LogoutHelper {

    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();

        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, ComposeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        //LogoutHelper.logout(this);
    }
}
