package be.fbousson.morsdeaud.common.crashlogging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.io.PrintWriter;
import java.io.StringWriter;

import be.fbousson.morsdeaud.common.R;


/**
 * Created by fbousson on 03/12/14.
 */
public class CrashLoggingUtility {

    public static class Configuration{

        private String emails;

        private String emailTitle;
        private String emailBody;

        private String notificationTitle;
        private String notificationBody;

        private int notificationDrawable;

        public Configuration(String emails, String emailTitle, String emailBody, String notificationTitle, String notificationBody, int notificationDrawable) {
            this.emails = emails;
            this.emailTitle = emailTitle;
            this.emailBody = emailBody;
            this.notificationTitle = notificationTitle;
            this.notificationBody = notificationBody;
            this.notificationDrawable = notificationDrawable;
        }

        public String getEmails() {
            return emails;
        }

        public String getEmailTitle() {
            return emailTitle;
        }

        public String getEmailBody() {
            return emailBody;
        }

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public String getNotificationBody() {
            return notificationBody;
        }

        public int getNotificationDrawable() {
            return notificationDrawable;
        }
    }


    private static Configuration _configuration;

    public static void trackCrash(final Context context, String emails){
        Configuration configuration = new Configuration(emails, context.getString(R.string.crashlog_emailTitle),context.getString(R.string.crashlog_emailBody),context.getString(R.string.crashlog_notificationTitle),context.getString(R.string.crashlog_notificationBody), R.drawable.app_explosion);
        trackCrash(context,  configuration);

    }


    public static void trackCrash(final Context context, Configuration configuration) {
        _configuration = configuration;
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e, context);
            }
        });


    }

    private static void handleUncaughtException(Thread thread, Throwable e, Context context) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String crashStackTrace = sw.toString();

        String userContext = getUserContext(context);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(_configuration.getNotificationDrawable())
                        .setContentTitle(_configuration.getNotificationTitle())
                        .setContentText(_configuration.getNotificationBody());
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", _configuration.getEmails(), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, _configuration.getEmailTitle());
        emailIntent.putExtra(Intent.EXTRA_TEXT, _configuration.getEmailBody() + userContext + crashStackTrace);
        ;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(emailIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());

        System.exit(1); // kill off the crashed app
    }

    private static String getUserContext(Context context) {
        String userContext = context.getString(R.string.settings_version, getVersionName(context)) +
                "\n" + context.getString(R.string.settings_revision, getVersionCode(context)) +
                "\n" + context.getString(R.string.android_information, android.os.Build.VERSION.RELEASE) +
                "\n" + context.getString(R.string.device_information, android.os.Build.MANUFACTURER, android.os.Build.MODEL) +
                "\n" + "CPU " + Build.CPU_ABI + " CPU 2 " + Build.CPU_ABI2 +
                "\n" + "Brand " + Build.BRAND +
                "\n" + "Device " + Build.DEVICE +
                "\n\n\n\n";
        return userContext;
    }


    public static String getVersion(Context context)
    {
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName + " :: " + pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            return "unknown";
        }
    }

    public static String getVersionName(Context context)
    {
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            return "unknown";
        }
    }

    public static String getVersionCode(Context context)
    {
        try
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "" + pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            return "unknown";
        }
    }

}
