package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-8
 * Description : Cette classe permet d'envoyer des notifications et de les programmers
 * Version : v5
 * Dernière modification : 2024-01-15
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationHelper {

    private static final String CHANNEL_ID = "your_channel_id";
    private static final String NOTIFICATION_TITLE = "notification_title";
    private static final String NOTIFICATION_CONTENT = "notification_content";

    // Méthode pour créer le canal de notification
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    // Méthode pour créer une notification immédiate
    public static void createNotification(Context context, String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }

        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            // Utiliser l'heure actuelle en millisecondes comme identifiant unique
            int notificationId = (int) (System.currentTimeMillis() & 0xfffffff);

            // Créer un Intent pour ouvrir MainActivity lorsque la notification est cliquée
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            int pendingIntentFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ?
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                    PendingIntent.FLAG_UPDATE_CURRENT;

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentFlag);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_sport)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // Configurer l'intent pour l'ouverture de l'activité
                    .setAutoCancel(true); // La notification disparaît après un clic sur celle-ci

            // Mettre à jour l'historique des notifications
            NotificationHistoryManager.addNotificationToHistory(context, new NotificationItem(title, content));

            try {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, builder.build());

                // Envoyer une diffusion pour mettre à jour l'interface utilisateur
                Intent updateIntent = new Intent("com.example.gamin_notification_v1.UPDATE_HISTORY");
                context.sendBroadcast(updateIntent);
            } catch (SecurityException e) {
                // Gérer l'exception si les permissions de notification sont refusées
            }
        }
    }



    // Méthode pour programmer l'envoi d'une notification
    public static void scheduleNotification(Context context, String title, String content, Calendar calendar) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            Intent intent = new Intent(context, NotificationPublisher.class);
            intent.putExtra(NOTIFICATION_TITLE, title);
            intent.putExtra(NOTIFICATION_CONTENT, content);

            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }

            // Utiliser le temps actuel en millisecondes comme identifiant unique
            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueInt, intent, flags);

            programAlarm(context, calendar, pendingIntent);
        }
    }


    private static void programAlarm(Context context, Calendar calendar, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
        // Si pas d'autorisation, on fait rien
    }

    ///////////////////// Redirection Permissions /////////////////////
    public static void CheckPermissionsNotification(Context context) {
        // Vérifie si l'application a l'autorisation d'envoyer des notifications
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            redirectToNotificationSettings(context);
        }

        // Vérifier si l'application peut programmer des alarmes exactes (Android 12 et versions ultérieures)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // Afficher une boîte de dialogue pour diriger vers les paramètres de l'application
                redirectToAlarmSettings(context);
            }
        }
    }
    private static void redirectToAlarmSettings(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Permission requise")
                .setMessage("Cette application a besoin de la permission de programmer des alarmes exactes pour envoyer des notifications à temps. Veuillez activer cette permission dans les paramètres.")
                .setPositiveButton("Paramètres", (dialog, which) -> {
                    Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(permissionIntent);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private static void redirectToNotificationSettings(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Permission requise")
                .setMessage("Les notifications sont actuellement désactivées pour cette application. Veuillez activer les notifications dans les paramètres.")
                .setPositiveButton("Paramètres", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                    context.startActivity(intent);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }


    ///////////////////// Action Arrière plan /////////////////////
    // Action lorsque notif recu
    public static class NotificationPublisher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra(NOTIFICATION_TITLE);
            String content = intent.getStringExtra(NOTIFICATION_CONTENT);

            // Créer la notification
            createNotification(context, title, content);
        }
    }


}
