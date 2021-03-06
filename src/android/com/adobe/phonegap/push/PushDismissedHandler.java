package com.adobe.phonegap.push;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PushDismissedHandler extends BroadcastReceiver implements PushConstants {
    private static String LOG_TAG = "Push_DismissedHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        FCMService fcm = new FCMService();
        String action = intent.getAction();
        int notID = intent.getIntExtra(NOT_ID, 0);

        if (action.equals(PUSH_DISMISSED)) {
            Log.d(LOG_TAG, "PushDismissedHandler = " + extras);
            Log.d(LOG_TAG, "not id = " + notID);

            fcm.setNotification(notID, "");
        }

        this.broadcastEvent(context.getApplicationContext(), notID);
    }

    /**
     * Enable rest of the app to register to push dismiss event
     */
    private void broadcastEvent(Context appContext, int notID) {
        Intent i = new Intent();
        i.setPackage(appContext.getPackageName());  // Required for BroadcastReceiver to work
        i.setAction(this.getDismissEventName(appContext));
        i.putExtra(NOT_ID, notID);
        appContext.sendBroadcast(i);
    }

    private static String dismissEventName = null;
    private String getDismissEventName(Context appContext) {
        if(PushDismissedHandler.dismissEventName == null) {
            PushDismissedHandler.dismissEventName = appContext.getPackageName() + ".action.push_dismissed";
        }

        return PushDismissedHandler.dismissEventName;        
    }
}