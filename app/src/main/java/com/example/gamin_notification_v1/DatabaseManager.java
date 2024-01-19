package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-15
 * Description : Gère les requetes vers la base de données
 * Version : v1
 * Dernière modification : 2024-01-15
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DatabaseManager {
    private Context context;
    public static RequestQueue requestQueue;

    public DatabaseManager(Context context)
    {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }



}
