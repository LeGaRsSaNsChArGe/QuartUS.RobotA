package com.example.interfacequartus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.interfacequartus.Fragment.Pieces;
import com.example.interfacequartus.Fragment.Planche;
import com.example.interfacequartus.Fragment.Transition;
import com.example.interfacequartus.Model.Quarto;
import com.example.interfacequartus.R;
import com.example.interfacequartus.databinding.ActivityPartieBinding;

public class Partie extends AppCompatActivity
{
    //Variables
    public ActivityPartieBinding binding;
    public Toolbar outils;

    Planche fragmentPlanche;
    Pieces fragmentPieces;

    Quarto partie;
    byte ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityPartieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentPartie = getIntent();
        int niveau = intentPartie.getIntExtra("niveau", -1);
        int mode = intentPartie.getIntExtra("mode", -1);

        partie = new Quarto(getApplicationContext(), niveau, mode);
        ID = 0;
        outils = findViewById(R.id.outils);

        fragmentPlanche = new Planche();
        fragmentPieces = new Pieces();
        remplaceFragment(fragmentPlanche);

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
    }

    private void remplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_partie, fragment);
        fragmentTransaction.commit();
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

    //Getters & Setters
    public Quarto getPartie()
    {
        return this.partie;
    }
    public byte getIDplusplus()
    {
        return ++ID;
    }
    public byte getID()
    {
        return ID;
    }

    public void setPartie(Quarto partie)
    {
        this.partie = partie;
    }
}