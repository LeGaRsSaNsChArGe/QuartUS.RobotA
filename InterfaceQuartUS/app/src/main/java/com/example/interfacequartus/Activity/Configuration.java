package com.example.interfacequartus.Activity;

import static com.example.interfacequartus.Activity.Accueil.activitePrecedente;
import static com.example.interfacequartus.Activity.Accueil.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfacequartus.R;
import com.example.interfacequartus.databinding.ActivityConfigurationBinding;

public class Configuration extends AppCompatActivity
{
    ActivityConfigurationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigurationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.listeNiveaux.setAdapter(new ArrayAdapter(this, R.layout.item_liste_defilante, getResources().getStringArray(R.array.niveau_du_robot)));
        binding.listeModes.setAdapter(new ArrayAdapter(this, R.layout.item_liste_defilante, getResources().getStringArray(R.array.mode_de_jeu)));

        if(!bluetooth.estActif())
            binding.listeNiveaux.setText(binding.listeNiveaux.getAdapter().getItem(0).toString());

        binding.boutonJeu.setOnClickListener(view ->
        {
            int niveau, mode;

            if(binding.listeNiveaux.getText().toString().equals(binding.listeNiveaux.getAdapter().getItem(0).toString()))
                niveau = 0;
            else if(binding.listeNiveaux.getText().toString().equals(binding.listeNiveaux.getAdapter().getItem(1).toString()))
                niveau = 1;
            else if(binding.listeNiveaux.getText().toString().equals(binding.listeNiveaux.getAdapter().getItem(2).toString()))
                niveau = 2;
            else if(binding.listeNiveaux.getText().toString().equals(binding.listeNiveaux.getAdapter().getItem(3).toString()))
                niveau = 3;
            else
            {
                niveau = -1;

                Toast message = Toast.makeText(getApplicationContext(),"Choisir le niveau du robot", Toast.LENGTH_SHORT);
                message.setGravity(Gravity.CENTER, 0, 0);
                message.show();
            }

            if(binding.listeModes.getText().toString().equals(binding.listeModes.getAdapter().getItem(0).toString()))
                mode = 0;
            else if(binding.listeModes.getText().toString().equals(binding.listeModes.getAdapter().getItem(1).toString()))
                mode = 1;
            else if(binding.listeModes.getText().toString().equals(binding.listeModes.getAdapter().getItem(2).toString()))
                mode = 2;
            else
            {
                mode = -1;

                Toast message = Toast.makeText(getApplicationContext(),"Choisir le mode de jeu", Toast.LENGTH_SHORT);
                message.setGravity(Gravity.CENTER, 0, 0);
                message.show();
            }

            if(niveau != -1 && mode != -1)
            {
                Intent intentPartie = new Intent(getApplicationContext(), Partie.class);
                intentPartie.putExtra("niveau", niveau);
                intentPartie.putExtra("mode", mode);

                startActivity(intentPartie);
            }
        });
    }

    @Override
    protected void onResume()
    {
        if(activitePrecedente.equals("Partie"))
            this.finish();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        /*if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }*/
        return false;
    }
}