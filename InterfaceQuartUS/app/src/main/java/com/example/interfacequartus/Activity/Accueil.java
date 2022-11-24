package com.example.interfacequartus.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.interfacequartus.Fragment.Transition;
import com.example.interfacequartus.Model.PeripheriqueBT;
import com.example.interfacequartus.Model.ReceptionFinEtapeBT;
import com.example.interfacequartus.R;
import com.example.interfacequartus.databinding.ActivityAccueilBinding;

public class Accueil extends AppCompatActivity {
    //Constantes
    public static final String DEBUG = "Débuggage";
    public static final String ERREUR = "Erreur";

    //Variables
    ActivityAccueilBinding binding;

    public static String activitePrecedente;
    public static int[] score = new int[2]; //0 = Humain, 1 = QuartUS

    public static PeripheriqueBT bluetooth;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
    {
        if(result.getResultCode() == Activity.RESULT_OK)
        {
            Log.e("Activity result","OK");
            // There are no request codes
            Intent data = result.getData();

            if(bluetooth.connexionBT())
            {
                binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
                binding.switchBluetooth.setChecked(true);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activitePrecedente = "aucune";
        //TODO : lecture et écriture fichiers
        score[0] = 0;
        score[1] = 0;

        bluetooth = new PeripheriqueBT(this, getApplicationContext(), "10:52:1C:62:DE:0A"); //TODO adresse du periphérique bluetooth du robot

        verificationBT();
        binding.switchBluetooth.setOnClickListener(view -> verificationBT());
        binding.regles.setOnClickListener(view -> startActivity(new Intent(Accueil.this, Regles.class)));
        binding.jouer.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Configuration.class)));
        //binding.credits.setOnClickListener(view -> startActivity(new Intent(Accueil.this, Credits.class)));
    }

    @Override
    protected void onResume()
    {
        activitePrecedente = "aucune";
        binding.scoreHumain.setText("" + score[0]);
        binding.scoreRobot.setText("" + score[1]);

        super.onResume();
    }

    //Méthodes
    private void verificationBT()
    {
        if(!binding.switchBluetooth.isChecked() || bluetooth.getAdapteurBT() == null || !bluetooth.verificationBluetooth())
        {
            bluetooth.setActif(false);
            binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
            binding.switchBluetooth.setChecked(false);

            if(bluetooth.getAdapteurBT() == null)
            {
                binding.switchBluetooth.setClickable(false);

                Toast message = Toast.makeText(getApplicationContext(),"Bluetooth non supporté", Toast.LENGTH_SHORT);
                message.show();
            }
            else if(!bluetooth.verificationBluetooth())
                activityResultLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
        else if(bluetooth.connexionBT())
            binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
        else
            binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_24));
    }

    /*private void verificationBT()
    {
        if(binding.switchBluetooth.isChecked())
        {
            binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_24));

            if(bluetooth.getAdapteurBT() == null)
            {
                Toast message = Toast.makeText(getApplicationContext(),"Bluetooth non supporté", Toast.LENGTH_SHORT);
                message.setGravity(Gravity.CENTER, 0, 0);
                message.show();
            }

            if(!bluetooth.verificationBluetooth())
            {
                activityResultLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));

                if(!bluetooth.verificationBluetooth())
                {
                    activityResultLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                    // TODO : verificationBT();
                }
            }
            else if(bluetooth.connexionBT())
                binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));
        }
        else
        {
            bluetooth.setActif(false);
            binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
        }
    }*/
}