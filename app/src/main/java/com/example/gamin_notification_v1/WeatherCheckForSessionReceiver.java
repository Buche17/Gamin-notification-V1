package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2023-01-13
 * Description : Action lorsque la notificaiton qui dit s'il pleut doit s'evoyer
 * Version : V2
 * Dernière modification : 2023-01-16
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

public class WeatherCheckForSessionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int type = BD_Requests.ReccupTypePersonne();

        // Récupération de l'heure de la séance de l'intent
        int heureSeance = intent.getIntExtra("heureSeance", 12); // 12 est une valeur par défaut au cas où l'extra n'est pas trouvé

        // Vérifiez la météo pour la séance
        if (LinkManagerNotification.CheckMeteo(heureSeance)) { // Si CheckMeteo() renvoie false, il fait pas beau, on fait :
            String baseMessage = context.getString(R.string.Si_Seance_si_il_pleut__le_matin_7h);
            // Sélectionner un message aléatoire en fonction du type
            int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
            String[] messages = context.getResources().getStringArray(arrayId);

            String randomMessage = messages[new Random().nextInt(messages.length)];
            String finalMessage = baseMessage.replace("$s", randomMessage);

            String titre = context.getString(R.string.Titre_Notif);
            NotificationHelper.createNotification(context, titre, finalMessage);
        }
        // Si checkSoleil() renvoie true, ne faites rien.
    }
}
