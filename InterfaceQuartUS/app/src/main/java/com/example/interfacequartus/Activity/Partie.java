package com.example.interfacequartus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.interfacequartus.Fragment.Pieces;
import com.example.interfacequartus.Fragment.Planche;
import com.example.interfacequartus.Model.Joueur;
import com.example.interfacequartus.Model.Piece;
import com.example.interfacequartus.Model.Quarto;
import com.example.interfacequartus.R;
import com.example.interfacequartus.databinding.ActivityPartieBinding;

public class Partie extends AppCompatActivity
{
    //Variables
    ActivityPartieBinding binding;

    Planche fragmentPlanche;
    Pieces fragmentPieces;

    //String joueurActif;
    public Quarto partie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityPartieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        remplaceFragment(new Planche());

        fragmentPlanche = new Planche();
        fragmentPieces = new Pieces();

        Intent intentPartie = getIntent();
        boolean bluetooth = intentPartie.getBooleanExtra("bluetooth", false);
        int niveau = intentPartie.getIntExtra("niveau", -1);
        int mode = intentPartie.getIntExtra("mode", -1);

        partie = new Quarto(getApplicationContext(), niveau, mode); /*TODO : joueurs*/

        binding.navigation.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.planche:
                    remplaceFragment(fragmentPlanche);
                    break;
                case R.id.pieces:
                    remplaceFragment(fragmentPieces);
                    break;
                default:
                    break;
            }

            return true;
        });

        binding.outils.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.aide:
                    //TODO aide
                    break;
                case R.id.regles:
                    startActivity(new Intent(Partie.this, Regles.class));
                    break;
                default:
                    break;
            }

            return true;
        });
    }

    private void remplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_partie, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }
}