package com.example.interfacequartus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.example.interfacequartus.R;
import com.example.interfacequartus.databinding.ActivityAccueilBinding;
import com.example.interfacequartus.databinding.ActivityPartieBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Accueil extends AppCompatActivity
{
    ActivityAccueilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.switchBluetooth.setOnClickListener(view ->
        {
            if (binding.switchBluetooth.isChecked())
            {
                //TODO Connexion Bluetooth

                //TODO si la connexion réussie alors
                //binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_connected_24));

                binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_24));
            }
            else
            {
                binding.iconBluetooth.setBackground(AppCompatResources.getDrawable(binding.iconBluetooth.getContext(), R.drawable.ic_baseline_bluetooth_disabled_24));
            }
        });

        binding.regles.setOnClickListener(view ->
        {
            startActivity(new Intent(Accueil.this, Regles.class));
        });

        binding.jouer.setOnClickListener(view ->
        {
            Intent intentConfiguration = new Intent(getApplicationContext(), Configuration.class);
            boolean bluetooth = false;

            if(binding.switchBluetooth.isChecked()/* && TODO connexion réussie*/)
                bluetooth = true;

            intentConfiguration.putExtra("bluetooth", bluetooth);

            startActivity(intentConfiguration);
        });

        binding.credits.setOnClickListener(view ->
        {
            startActivity(new Intent(Accueil.this, Credits.class));
        });
    }
}