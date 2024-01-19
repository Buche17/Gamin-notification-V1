package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-12
 * Description : Programme ou envoi les notifications au bon moment
 * Version : v12
 * Dernière modification : 2024-01-16
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.Random;

public class NotificationScenario {



    /////////////////// Notification qui se programment si séance ///////////////////
    public static void NotificationCreationSeance(Context context, Calendar calendar){
        // Regroupement de toutes les fonctions qui devront être appelée au moment où une séance est crée
        Notification3heuresAvantSeance(context, calendar);
        NotificationVeilleSeance(context, calendar);
        Notification2heuresApresSeance(context, calendar);
        NotifCheckPluieSeance(context, calendar);

    }


    public static void Notification3heuresAvantSeance(Context context, Calendar calendar) {
        int type = BD_Requests.ReccupTypePersonne();

        // Retirer 3 heures du calendar
        calendar.add(Calendar.HOUR_OF_DAY, -3);

        // Récupérer les messages de base
        String[] baseMessages = context.getResources().getStringArray(R.array.Si_Seance__3h_avant);

        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        // Boucle pour envoyer chaque message
        for (String baseMessage : baseMessages) {
            String randomMessage = messages[new Random().nextInt(messages.length)];
            String finalMessage = baseMessage.replace("$s", randomMessage);

            String titre = context.getString(R.string.Titre_Notif);
            // Appeler scheduleNotification de NotificationHelper pour chaque message
            NotificationHelper.scheduleNotification(context, titre, finalMessage, calendar);
        }
    }

    public static void NotificationVeilleSeance(Context context, Calendar calendar) {
        int type = BD_Requests.ReccupTypePersonne();

        Calendar notificationTime = (Calendar) calendar.clone();
        notificationTime.add(Calendar.DAY_OF_MONTH, -1); // La veille
        notificationTime.set(Calendar.HOUR_OF_DAY, 19);
        notificationTime.set(Calendar.MINUTE, 0);
        notificationTime.set(Calendar.SECOND, 0);

        String baseMessage = context.getString(R.string.Si_Seance__la_veille_19h);

        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        String randomMessage = messages[new Random().nextInt(messages.length)];
        String finalMessage = baseMessage.replace("$s", randomMessage);

        String titre = context.getString(R.string.Titre_Notif);
        NotificationHelper.scheduleNotification(context, titre, finalMessage, notificationTime);
    }

    /// Check Meteo Pleut si séance à 7h ///
    public static void NotifCheckPluieSeance(Context context, Calendar calendar) {

        // Check l'autorisation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !context.getSystemService(AlarmManager.class).canScheduleExactAlarms()) { // check autorisation
            return;
        }

        // Vérifiez si le calendrier est réglé pour une date future
        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            return; // Ne rien faire si la date de la séance est passée
        }

        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        Intent intent = new Intent(context, WeatherCheckForSessionReceiver.class);

        // Ajout de l'heure de la séance comme extra dans l'intent, cette extra est lié à son intent et prend en compte le parallélisme des notifications
        int heureSeance = calendar.get(Calendar.HOUR_OF_DAY);
        intent.putExtra("heureSeance", heureSeance);

        // Utiliser le temps de la séance comme identifiant unique
        int uniqueId = (int) (calendar.getTimeInMillis() & 0xfffffff);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueId, intent, flags);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void Notification2heuresApresSeance(Context context, Calendar calendar) {
        int type = BD_Requests.ReccupTypePersonne();

        // Ajoute 2 heures au calendar
        calendar.add(Calendar.HOUR_OF_DAY, 2);

        // Récupérer les messages de base
        String baseMessage = context.getString(R.string.fin_seance);
        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        String randomMessage = messages[new Random().nextInt(messages.length)];
        String finalMessage = baseMessage.replace("$s", randomMessage);

        String titre = context.getString(R.string.Titre_Notif);
        NotificationHelper.scheduleNotification(context, titre, finalMessage, calendar);

    }

    /////////////////// Notification a evoyer lors d'un evenement ///////////////////

    public static void NotifGagneBadge(Context context) {
        int type = BD_Requests.ReccupTypePersonne();

        // Récupérer les messages de base
        String baseMessage = context.getString(R.string.gagne_badge);
        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        String randomMessage = messages[new Random().nextInt(messages.length)];
        String finalMessage = baseMessage.replace("$s", randomMessage);

        String titre = context.getString(R.string.Titre_Notif);
        NotificationHelper.createNotification(context, titre, finalMessage);
    }

    public static void NotifAtteintObjSemaine(Context context) {
        int type = BD_Requests.ReccupTypePersonne();

        // Récupérer les messages de base
        String baseMessage = context.getString(R.string.atteint_obj_semaine);
        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        String randomMessage = messages[new Random().nextInt(messages.length)];
        String finalMessage = baseMessage.replace("$s", randomMessage);

        String titre = context.getString(R.string.Titre_Notif);
        NotificationHelper.createNotification(context, titre, finalMessage);
    }

    /////////////////// Notification qui se programment quoi qu'il arrive ///////////////////

    public static void NotifCheckBeau(Context context) {
        // Notification qui va tous les matin à 7 heure vérifier si il fait beau

        // Check l'autorisation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !context.getSystemService(AlarmManager.class).canScheduleExactAlarms()) { // check autorisation
            return;
        }

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, 7);
        notificationTime.set(Calendar.MINUTE, 0);
        notificationTime.set(Calendar.SECOND, 0);
        notificationTime.set(Calendar.MILLISECOND, 0);

        if (notificationTime.before(Calendar.getInstance())) {
            notificationTime.add(Calendar.DAY_OF_MONTH, 1); // Programmer pour le lendemain si 7h est déjà passé
        }

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        int IdNotifBeau = 10;
        Intent intent = new Intent(context, WeatherCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, IdNotifBeau, intent, flags);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);
    }

    public static void NotifCheckBilanSemaine(Context context) {
        // Notification qui va tous les samedi soir à 19 heure avec les informations bilan de la semaine

        // Comme cette fonction a besoin de passer un intent dans la pogrammation de l'alarme, elle ne peut pas utiliser
        // la fonction qui programme classiquement les notifications et donc faisant l'alarme elle meme, il faut check l'autorisation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !context.getSystemService(AlarmManager.class).canScheduleExactAlarms()) { // check autorisation
            return;
        }

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, 19);
        notificationTime.set(Calendar.MINUTE, 0);
        notificationTime.set(Calendar.SECOND, 0);
        notificationTime.set(Calendar.MILLISECOND, 0);

        // Définir le jour de la notification pour le prochain samedi
        int dayOfWeek = notificationTime.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextSaturday = Calendar.SATURDAY - dayOfWeek;
        if (daysUntilNextSaturday <= 0) {
            daysUntilNextSaturday += 7; // S'il est déjà samedi, programmer pour le samedi suivant
        }
        notificationTime.add(Calendar.DAY_OF_YEAR, daysUntilNextSaturday);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        int IdNotifBilan = 11;
        Intent intent = new Intent(context, BilanSemaineCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, IdNotifBilan, intent, flags);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);
    }
}


