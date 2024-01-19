package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-12
 * Description : Fait le lien entre la partie notification et les autres parties du projet
 * Version : v4
 * Dernière modification : 2024-01-16
 */

import android.content.Context;
import java.util.Calendar;


public class LinkManagerNotification {
    // Cette classe a pour but de faire les liens nécessaires au fonctionnement des notifications de l'application
    // Il s'agit de lier les notification aux autres parties de l'application dont elles ont besoins

    /////////////////// Rend accessible les fonctions de Notification ///////////////////
    public static void NotificationSeance(Context context, Calendar calendar) {
        // A appeler à cahque création de séance
        NotificationScenario.NotificationCreationSeance(context, calendar);
    }

    public static void NotificationGagneBadge(Context context) {
       // A appeler si la partie qui gère les badges détecte donne un nouveau badge
        NotificationScenario.NotifGagneBadge(context);
    }

    public static void NotifAtteintObjSemaine(Context context) {
        // Lorsqu'une séance est lancé et qu'on détect qu'on a atteint les objectifs de la semaine
        NotificationScenario.NotifAtteintObjSemaine(context);
    }

    /////////////////// Reccup donnée d'autre partie ///////////////////

    public static boolean CheckMeteo(int heure){
        // Il faut lier cette fonction à la fonction qui se trouve dans la partie météo qui check la meteo en fonction d'une heure
        //Meteo.Cheeckjsp(heure);
        return true; // a retirer
    }

}
