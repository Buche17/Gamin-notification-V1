package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-13
 * Description : Action lorsque la notificaiton qui dit s'il fait beau doit s'evoyer
 * Version : v2
 * Dernière modification : 2024-01-16
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

public class WeatherCheckReceiver extends BroadcastReceiver {
    // Cette classe envoie ou non la notification qui dit s'il fait beau adj

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = BD_Requests.ReccupTypePersonne();
        int heureApresMidi = 15;

        if (LinkManagerNotification.CheckMeteo(heureApresMidi)) { // Si vrai (il fait beau) alors :

            String baseMessage = context.getString(R.string.si_fait_beau__le_matin_7h);

            // Sélectionner un message aléatoire en fonction du type
            int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
            String[] messages = context.getResources().getStringArray(arrayId);

            String randomMessage = messages[new Random().nextInt(messages.length)];
            String finalMessage = baseMessage.replace("$s", randomMessage);

            String titre = context.getString(R.string.Titre_Notif);
            NotificationHelper.createNotification(context, titre, finalMessage);
        }

        // Reprogrammer pour le jour suivant si nécessaire
        NotificationScenario.NotifCheckBeau(context);
    }
}

