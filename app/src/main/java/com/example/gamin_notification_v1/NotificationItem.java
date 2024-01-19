package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-11
 * Description : Classe qui décrit le contenu des notifications, c'est utiliser pour enregistrer un historique des notifications
 * Version : v1
 * Dernière modification : 2024-01-11
 */

public class NotificationItem {
    private String title;
    private String content;

    // Constructeur et getters
    public NotificationItem(String title, String content){
        this.title = title;
        this.content = content;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
}

