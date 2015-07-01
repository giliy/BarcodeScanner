package utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by gili on 6/28/2015.
 */
public class L {
    public static void m(String message) {
        Log.d("LOG_CHECK: ", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}
