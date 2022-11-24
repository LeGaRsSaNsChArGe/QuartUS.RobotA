package com.example.interfacequartus.Model;
import com.example.interfacequartus.Activity.Accueil;
import com.example.interfacequartus.Fragment.Transition;


import static com.example.interfacequartus.Activity.Accueil.DEBUG;
import static com.example.interfacequartus.Activity.Accueil.bluetooth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class ReceptionFinEtapeBT extends AsyncTask<Void, Void, Void>
{
    //Constantes
    public static final int TEMPS_DEPLACEMENT = 120;

    //Variables
    Context context;
    Transition transition;

    boolean confirmation;
    InputStream inStream;
    Byte ID;

    //Constructeurs
    public ReceptionFinEtapeBT()
    {
        this.inStream = null;
        this.ID = null;

        this.confirmation = false;

        this.context = null;
        this.transition = null;
    }
    public ReceptionFinEtapeBT(Context context)
    {
        this.inStream = null;
        this.ID = null;

        this.confirmation = false;

        this.context = context;
        this.transition = null;
    }
    public ReceptionFinEtapeBT(InputStream inStream, Byte ID, Context context)
    {
        this.inStream = inStream;
        this.ID = ID;

        this.confirmation = false;

        this.context = context;
        this.transition = null;
    }

    //Getteurs & Setteurs
    public boolean estConfirmation()
    {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation)
    {
        this.confirmation = confirmation;
    }

    //Méthodes
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        transition = new Transition(context);
        transition.setCancelable(false);
        transition.show();
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            int compteur = 0;
            int read = 0;

            Log.d(DEBUG, "...Début de l'attente...");
            do
            {
                //Log.d(DEBUG, "...");
                Log.d(DEBUG, String.valueOf(read));
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                if(inStream.available() > 0)
                    read = inStream.read();

            }while((read &32) != ((int)this.ID) && (read &128) != 128 && (compteur++ != TEMPS_DEPLACEMENT));
            Log.d(DEBUG, "...Fin de l'attente...");

            this.confirmation = compteur != TEMPS_DEPLACEMENT;

            if(compteur >= TEMPS_DEPLACEMENT)
                bluetooth.setActif(false);

        }catch (IOException e)
        {
            Log.d(DEBUG, "Erreur pendant l'attente de réception de fin d'étape");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void ignore)
    {
        transition.dismiss();

        Toast message;
        if(bluetooth.estActif())
            message = Toast.makeText(context,"QuartUS a terminé son déplacement", Toast.LENGTH_SHORT);
        else
            message = Toast.makeText(context,"ERREUR de transmission Bluetooth\nBluetooth désactivé!.", Toast.LENGTH_SHORT);

        message.show();
    }
}
