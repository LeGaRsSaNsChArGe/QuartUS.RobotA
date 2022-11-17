package com.example.interfacequartus.Model;

import static com.example.interfacequartus.Activity.Accueil.DEBUG;
import static com.example.interfacequartus.Activity.Accueil.ERREUR;
import static com.example.interfacequartus.Activity.Accueil.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.interfacequartus.Activity.Accueil;
import com.example.interfacequartus.Fragment.Transition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PeripheriqueBT
{
    //Constantes
    //public static final int NULL = 0;
    //public static final int TRUE = 1;
    //public static final int FALSE = -1;
    public static final int TEMPS_DEPLACEMENT = 120;

    public static final int REQUEST_ENABLE_BT = 1;
    public static final UUID MON_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//Universel UUID

    //Variables
    BluetoothAdapter adapteurBT;
    BluetoothSocket priseBT;
    OutputStream outStream;
    InputStream inStream;

    String addresseMac;// Ex: "20:16:09:12:32:93"

    Accueil accueil;
    Context context;
    boolean connexionEtablie;

    //Constructeurs
    public PeripheriqueBT()
    {
        this.adapteurBT = null;
        this.priseBT = null;
        this.outStream = null;
        this.inStream = null;

        this.addresseMac = "00:00:00:00:00:00";

        this.accueil = null;
        this.context = null;
        this.connexionEtablie = false;
    }

    public PeripheriqueBT(Accueil accueil, Context context, String adresseMac)
    {
        this.adapteurBT = BluetoothAdapter.getDefaultAdapter();
        if (this.adapteurBT == null)
            Log.e(Accueil.ERREUR, "Bluetooth Non supporté. Abandon de la misson.");

        this.priseBT = null;
        this.outStream = null;
        this.inStream = null;

        this.addresseMac = adresseMac;

        this.accueil = accueil;
        this.context = context;
        this.connexionEtablie = false;
    }

    //Getteurs & setteurs
    public BluetoothAdapter getAdapteurBT() {
        return adapteurBT;
    }

    public BluetoothSocket getPriseBT() {
        return priseBT;
    }

    public OutputStream getOutStream() {
        return outStream;
    }

    public String getAddresseMac() {
        return addresseMac;
    }

    public Context getContext() {
        return context;
    }

    public boolean connexionEtablie() {
        return connexionEtablie;
    }

    public void setAdapteurBT(BluetoothAdapter adapteurBT) {
        this.adapteurBT = adapteurBT;
    }

    public void setPriseBT(BluetoothSocket priseBT) {
        this.priseBT = priseBT;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    public void setAddresseMac(String addresseMac) {
        this.addresseMac = addresseMac;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //Méthodes
    public boolean verificationBluetooth() {
        if (adapteurBT.isEnabled()) {
            Toast message = Toast.makeText(context, "...Bluetooth activé...", Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER, 0, 0);
            message.show();
            return true;
        } else
            return false;
    }

    public boolean connexionBT() {
        BluetoothDevice device = adapteurBT.getRemoteDevice(addresseMac);

        try {
            /*if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    ActivityCompat.requestPermissions(accueil, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);*/

            priseBT = device.createRfcommSocketToServiceRecord(MON_UUID);
        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Création de la connection échouée: " + e.getMessage() + ".");
            return false;
        }

        adapteurBT.cancelDiscovery();

        Log.d(DEBUG, "...Connection à l'appareil...");
        try
        {
            priseBT.connect();
            Log.d(DEBUG, "...Connection établie et lien de données ouvert...");
        }catch(IOException e)
        {
            try
            {
                priseBT.close();
            }catch(IOException e2)
            {
                Log.e(Accueil.ERREUR,"Fermeture de la connection échouée" + e2.getMessage() + ".");
            }
            return false;
        }

        try
        {
            outStream = priseBT.getOutputStream();
        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Création du OutputStream échouée:" + e.getMessage() + ".");
            return false;
        }

        try
        {
            inStream = priseBT.getInputStream();
        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Création du InputStream échouée:" + e.getMessage() + ".");
            return false;
        }

        this.connexionEtablie = true;
        return true;
    }

    public boolean envoieDonnees(byte ID, int deplacement, int r, int c, int victoire, int tourJoueur, FragmentManager fragmentManager)
    {
        byte[] bytesBuffer = {ID, (byte)deplacement, (byte)r, (byte)c, (byte)victoire, (byte)tourJoueur, (byte)'#'};
        Log.d(DEBUG, "...Données envoyées: " + bytesBuffer + "...");

        try
        {
            outStream.write(bytesBuffer);
            if(recoieConfirmation(ID, bytesBuffer, fragmentManager))
                return recoieFinEtape(ID);
            else
                return false;
        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Erreur pendant l'écriture: " + e.getMessage());
        }

        return false;
    }

    private boolean recoieConfirmation(byte ID, byte[] bytesBuffer, FragmentManager fragmentManager)
    {
        try
        {
            int compteur = 0;

            int read = -1;

            //Transition transition = new Transition();
            //transition.setCancelable(false);
            //transition.show(fragmentManager, "Transition");

            while(((read = inStream.read()) &63) != ID && compteur != 10)
            {
                Log.d(DEBUG, "...ID: " + ID + "\t\tDonnées recues: " +(read&63) + "...");
                try
                {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                //outStream.write(bytesBuffer);

                compteur++;
            }

            //transition.dismiss();

            return compteur != 10;

        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Erreur pendant la lecture");
        }

        return false;
    }

    private boolean recoieFinEtape(byte ID)
    {
        try
        {
            int compteur = 0;
            int read = -1;

            while(((read = inStream.read()) &32) != ID && (read &128) != 128 && compteur != TEMPS_DEPLACEMENT)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                compteur++;
            }

            return compteur != TEMPS_DEPLACEMENT;

        }catch(IOException e)
        {
            Log.e(Accueil.ERREUR,"Erreur pendant la lecture");
        }

        return false;
    }
}