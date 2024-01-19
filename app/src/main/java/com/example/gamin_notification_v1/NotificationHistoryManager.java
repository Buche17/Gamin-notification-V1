package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-11
 * Description : Gère l'hitorique des 10 dernières notifications envoyés
 * Version : v1
 * Dernière modification : 2024-01-11
 */

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryManager {

    private static final String PREFS_NAME = "notification_history";
    private static final String KEY_HISTORY = "history";

    // Sauvegarder l'historique des notifications
    public static void saveNotificationHistory(Context context, List<NotificationItem> history) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(history);
        editor.putString(KEY_HISTORY, json);
        editor.apply();
    }

    // Récupérer l'historique des notifications
    public static List<NotificationItem> getNotificationHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_HISTORY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Ajouter une notification à l'historique
    public static void addNotificationToHistory(Context context, NotificationItem notification) {
        List<NotificationItem> history = getNotificationHistory(context);

        // Ajouter la nouvelle notification et garder uniquement les 10 dernières
        history.add(notification);
        if (history.size() > 10) {
            history.remove(0); // Supprimer la plus ancienne
        }

        saveNotificationHistory(context, history);
    }
}
