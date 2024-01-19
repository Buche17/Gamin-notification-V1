package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-12
 * Description : Cette classe a été ajouté au execution faite au démarrage, elle sert à vérifier
 *               que la programmation des notifications est toujours opérationnelle
 * Version : v1
 * Dernière modification : 2024-01-14
 */

import android.app.Application;



public class AppCheckNotifDemarrage extends Application {

    // Cette classe sert uniquement à vérifier que la ou les notification qui s'envoi tous les jours sont toujours programmé
    // Elle fait la vérification chaque fois que l'application se lance
    // En effet, AppCheckNotifDemarrage a été ajoutée dans le fichier AndroidManifest comme code à lander au chargement

    @Override
    public void onCreate() {
        super.onCreate();

        // Fonction à lancer au demarrage
        NotificationScenario.NotifCheckBeau(this);
        NotificationScenario.NotifCheckBilanSemaine(this);

    }
}
