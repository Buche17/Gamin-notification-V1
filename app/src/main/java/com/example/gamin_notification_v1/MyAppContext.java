package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-15
 * Description : Permet de reccupérer un context lorsque celui-ci n'est pas obtenable ailleurs, ceci peut être utile
 *               pour codes qui ne sont pas des interfaces graphiques et qui ont besoin d'un contexte, ici seul les
 *               les fonctions qui font des requêtes de BD en ont besoins
 * Version : v1
 * Dernière modification : 2024-01-15
 */

import android.app.Application;
import android.content.Context;

public class MyAppContext extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyAppContext.context = getApplicationContext();
    }

    public static Context GetAppContext() {
        return MyAppContext.context;
    }
}

