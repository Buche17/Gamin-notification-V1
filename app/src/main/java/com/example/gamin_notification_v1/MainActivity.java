package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2023-11-28
 * Description : Partie graphique à afficher sur la page d'accueil, comprennant l'historique des notifications
 * Version : v9
 * Dernière modification : 2024-01-15
 */

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver updateUIReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////// Historique notification accueil /////////////////////////
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<NotificationItem> history = NotificationHistoryManager.getNotificationHistory(this);

        // Inverser l'ordre de la liste pour afficher les notifications les plus récentes en premier
        Collections.reverse(history);

        for (NotificationItem item : history) {
            TextView titleTextView = new TextView(this);
            titleTextView.setText(item.getTitle());
            titleTextView.setTextSize(18);
            linearLayout.addView(titleTextView);

            TextView textView = new TextView(this);
            textView.setText(item.getContent());
            textView.setTextSize(16);
            linearLayout.addView(textView);
        }

        NotificationHelper.CheckPermissionsNotification(this);

        // Initialiser et enregistrer le BroadcastReceiver
        initUIUpdateReceiver();
    }

    ///////////////////////// Actualisation automatique Historique notification accueil /////////////////////////
    private void initUIUpdateReceiver() {
        updateUIReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Mettre à jour l'affichage de l'historique des notifications
                updateNotificationHistoryDisplay();
            }
        };
        IntentFilter filter = new IntentFilter("com.example.gamin_notification_v1.UPDATE_HISTORY");
        registerReceiver(updateUIReceiver, filter);
    }

    private void updateNotificationHistoryDisplay() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        List<NotificationItem> history = NotificationHistoryManager.getNotificationHistory(this);

        // Inverser l'ordre de la liste pour afficher les notifications les plus récentes en premier
        Collections.reverse(history);

        for (NotificationItem item : history) {
            TextView titleTextView = new TextView(this);
            titleTextView.setText(item.getTitle());
            titleTextView.setTextSize(18);
            linearLayout.addView(titleTextView);

            TextView textView = new TextView(this);
            textView.setText(item.getContent());
            textView.setTextSize(16);
            linearLayout.addView(textView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateUIReceiver); // N'oubliez pas de désenregistrer le BroadcastReceiver
    }

    ///////////////////////// Fonction de test pour envoyer des notifications /////////////////////////
    //////////////////////////////////////////// A retirer ////////////////////////////////////////////
    public void onSendNotificationClick(View view) {
        NotificationScenario.NotifGagneBadge(this);
    }

    /*public void onSendNotificationClick(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        calendar.add(Calendar.HOUR_OF_DAY, -3);

        NotificationScenario.Notification3heuresAvantSeance(this, calendar);
    }*/


}