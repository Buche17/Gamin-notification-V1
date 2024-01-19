package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-14
 * Description : Action lorsque la notificaiton qui fait le bilan de la semaine doit s'evoyer
 * Version : v3
 * Dernière modification : 2024-01-16
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;


public class BilanSemaineCheckReceiver extends BroadcastReceiver {
    // Cette classe envoie ou non la notification qui dit s'il fait beau adj

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = BD_Requests.ReccupTypePersonne();
        int distanceEnKM = BD_Requests.GetDistanceParcourueEnKM();
        int minutesActivite = BD_Requests.GetMinutesActivite();

        float poidUtilisateur =  BD_Requests.GetPoidUtilisateur();
        int calorie = calculCaloriesBrulees(poidUtilisateur, distanceEnKM, minutesActivite);


        String baseMessage = context.getString(R.string.bilan_fin_semaine);
        // Sélectionner un message aléatoire en fonction du type
        int arrayId = (type == 1) ? R.array.motivation_messages_promotion : R.array.motivation_messages_prevention;
        String[] messages = context.getResources().getStringArray(arrayId);

        String randomMessage = messages[new Random().nextInt(messages.length)];
        String finalMessage = baseMessage.replace("$s", randomMessage);
        finalMessage = finalMessage.replace("$d", String.valueOf(distanceEnKM));
        finalMessage = finalMessage.replace("$m", String.valueOf(minutesActivite));
        finalMessage = finalMessage.replace("$c", String.valueOf(calorie));


        String titre = context.getString(R.string.Titre_Notif);
        NotificationHelper.createNotification(context, titre, finalMessage);


        // Reprogrammer pour le jour suivant si nécessaire
        NotificationScenario.NotifCheckBilanSemaine(context);
    }

    public static int calculCaloriesBrulees(float poids, int distance, int dureeMinutes) {
        // Convertir la durée en heures pour calculer la vitesse
        double dureeHeures = dureeMinutes / 60.0;

        // Calculer la vitesse moyenne en km/h
        double vitesseKmH = distance / dureeHeures;

        // Estimer les METs en fonction de la vitesse
        double mets = estimerMETs(vitesseKmH);

        // Calculer les calories brûlées
        double caloriesParMinute = (mets * poids * 3.5) / 200.0;
        double caloriesTotales = caloriesParMinute * dureeMinutes;

        return (int) caloriesTotales;
    }

    private static double estimerMETs(double vitesseKmH) {
        // Cette fonction est une estimation simple basée sur des plages de vitesse courantes
        // Les valeurs METs doivent être ajustées selon des données plus précises si disponibles

        if (vitesseKmH < 8) {
            return 7.0; // Marche rapide
        } else if (vitesseKmH < 9.5) {
            return 8.3; // Jogging
        } else if (vitesseKmH < 11) {
            return 9.8; // Course à 9-10 km/h
        } else if (vitesseKmH < 12.5) {
            return 11.0; // Course à 11-12 km/h
        } else {
            return 11.8; // Course rapide
        }
    }

}
