package com.example.gamin_notification_v1;

/*
 * Fichier Java créé par : Axel
 * Groupe : Intégrateur
 * Intégrateur : Axel
 * Date de création : 2024-01-15
 * Description : Reccupère les données necessaire sur la base de données
 * Version : v2
 * Dernière modification : 2024-01-17
 */


import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;


public class BD_Requests {
    private String username;
    private  String password;


    // Cette classe s'occupe de réccupérer les données de la BD


    public static int ReccupTypePersonne(){
        return 2;
    }

    public static int GetDistanceParcourueEnKM(){
        //Prend distance
        return 10; // a retirer
    }
    public static int GetMinutesActivite(){
        //Prend distance : liste de
        return 10; // a retirer
    }
    public static float GetPoidUtilisateur(){
        //Prend distance
        return 10; // a retirer
    }

    public void connectUser() {
        String url = "https://projet-android-garmin.iut-orsay.fr/API/actions/connectUser.php";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error")) {
                                String debugInfo = response.getString("error");
                                System.out.println("DebugInfo -> ' " + debugInfo + " '");
                            }
                            else
                            {
                                System.out.println("Erreur sur le PHP");
                            }
                            System.out.println("DebugInfo -> ' " + response.getString("pass1") + " '");
                            System.out.println("DebugInfo -> ' " + response.getString("pass2") + " '");
                            System.out.println("Status connection -> ' " + response.getString("succes") + " '");


                            Toast.makeText(MyAppContext.GetAppContext(), "Opération réussite", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Toast.makeText(MyAppContext.GetAppContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyAppContext.GetAppContext(), "Une erreur s'est produite. 321", Toast.LENGTH_LONG).show();
                }
            }
        });

        DatabaseManager.requestQueue.add(jsonObjectRequest);
    }



}
