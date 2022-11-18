package com.example.interfacequartus.Model;
import com.example.interfacequartus.Activity.Partie;


import static com.example.interfacequartus.Activity.Accueil.DEBUG;
import static com.example.interfacequartus.Model.PeripheriqueBT.TEMPS_DEPLACEMENT;
import static com.example.interfacequartus.Activity.Partie.robotPret;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

class TimeoutBT extends AsyncTask<Void, Void, Void>  {


    InputStream inStream;
    Byte id;

    public TimeoutBT(InputStream inStream,Byte id){
        this.inStream = inStream;
        this.id = id;
    }



    @Override
    protected Void doInBackground(Void... voids) {

        int compteur = 0;
        int read = -1;
        Log.d(DEBUG, "Background starte");
        try {
            while(((read = inStream.read()) &32) != id && (read &128) != 128 && (compteur != TEMPS_DEPLACEMENT))
            {
                Log.d(DEBUG, String.valueOf(read));
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                compteur++;
            }
        }catch (IOException e){
            Log.d(DEBUG, "IO");
        }


        if(compteur != TEMPS_DEPLACEMENT){
            robotPret = true;
        }else{
            robotPret = false;


        }

        return null;
    }
}
