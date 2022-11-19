package com.example.interfacequartus.Fragment;

import static com.example.interfacequartus.Activity.Accueil.bluetooth;
import static com.example.interfacequartus.Model.Quarto.ERREUR_CONFIRMATION;
import static com.example.interfacequartus.Model.Quarto.ERREUR_SELECTION;
import static com.example.interfacequartus.Model.Quarto.ERREUR_VIDE;
import static com.example.interfacequartus.Model.Quarto.OK;
import static com.example.interfacequartus.Model.Quarto.PIECES;
import static com.example.interfacequartus.Model.Quarto.PRENDRE_PIECE;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.interfacequartus.Activity.Partie;
import com.example.interfacequartus.Activity.Regles;
import com.example.interfacequartus.BuildConfig;
import com.example.interfacequartus.ImageViewCarrePortrait;
import com.example.interfacequartus.Model.Case;
import com.example.interfacequartus.R;

import java.util.concurrent.TimeUnit;

public class Pieces extends Fragment
{
    Partie parent;
    ImageViewCarrePortrait[][] imagePieces = new ImageViewCarrePortrait[4][4];

    public Pieces() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pieces, container, false);

        parent = (Partie) requireActivity();

        parent.outils.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.aide:

                    for(int r = 0 ; r < 4 ; r++)
                        for(int c = 0 ; c < 4 ; c++)
                            if(parent.getPartie().prendrePieceEstSuggestion(r, c) && isVisible())
                                setSuggestion(r, c);
                    break;
                case R.id.regles:
                    startActivity(new Intent(requireActivity(), Regles.class));
                    break;
                default:
                    break;
            }

            return true;
        });

        for(int r = 0 ; r < 4 ; r++)
        {
            for (int c = 0; c < 4; c++)
            {
                imagePieces[r][c] = view.findViewById(getResources().getIdentifier("c" + r + c, "id", BuildConfig.APPLICATION_ID));

                Case t_case = parent.getPartie().getCase(PIECES, r, c);
                if(!t_case.estVide())
                    imagePieces[r][c].setImageDrawable(t_case.getPiece().getDrawable());

                int t_r = r;
                int t_c = c;
                imagePieces[r][c].setOnClickListener(view1 ->
                {
                    Toast message;
                    int cas = ERREUR_CONFIRMATION;
                    if(bluetooth.estConfirmationFinEtape() && bluetooth.estActif())
                        cas = parent.getPartie().prendreSelection(t_r, t_c);
                    else if(!bluetooth.estActif())
                        cas = parent.getPartie().prendreSelection(t_r, t_c);

                    switch(cas)
                    {
                        case OK:
                            for(int r1 = 0 ; r1 < 4 ; r1++)
                                for (int c1 = 0; c1 < 4; c1++)
                                    if(!parent.getPartie().getCase(PIECES, r1, c1).estVide())
                                        imagePieces[r1][c1].setImageDrawable(AppCompatResources.getDrawable(parent.getApplicationContext(), getResources().getIdentifier("p_" + r1 + c1, "drawable", BuildConfig.APPLICATION_ID)));

                            imagePieces[t_r][t_c].setImageResource(android.R.color.transparent);

                            if(bluetooth.estActif())
                            {
                                bluetooth.setActif(bluetooth.envoieDonnees(parent.getIDplusplus(), PRENDRE_PIECE, t_r, t_c, OK, parent.getPartie().getJoueurActif(), getFragmentManager()));
                                if(!bluetooth.estActif())
                                {
                                    message = Toast.makeText(getContext(),"ERREUR de transmission Bluetooth\nBluetooth désactivé!", Toast.LENGTH_SHORT);
                                    message.setGravity(Gravity.CENTER, 0, 0);
                                    message.show();
                                }
                            }
                            break;
                        case ERREUR_VIDE:
                            message = Toast.makeText(getContext(),"Pièce déjà jouée...", Toast.LENGTH_SHORT);
                            message.setGravity(Gravity.CENTER, 0, 0);
                            message.show();
                            break;
                        case ERREUR_SELECTION:
                            message = Toast.makeText(getContext(),"Pièce déjà sélectionnée...", Toast.LENGTH_SHORT);
                            message.setGravity(Gravity.CENTER, 0, 0);
                            message.show();
                            break;
                        case ERREUR_CONFIRMATION:
                            message = Toast.makeText(getContext(),"Robot en déplacement, attendez...", Toast.LENGTH_SHORT);
                            message.setGravity(Gravity.CENTER, 0, 0);
                            message.show();
                            break;
                        default:
                            break;
                    }
                });
            }
        }

        return view;
    }

    //Méthodes
    private void setSuggestion(int r, int c)
    {
        Drawable image = AppCompatResources.getDrawable(parent.getApplicationContext(), getResources().getIdentifier("p_" + r + c, "drawable", BuildConfig.APPLICATION_ID));
        Drawable contour = AppCompatResources.getDrawable(parent.getApplicationContext(), R.drawable.p_suggestion);

        LayerDrawable finalDrawable = new LayerDrawable(new Drawable[] {image, contour});
        finalDrawable.setLayerGravity(0, Gravity.CENTER);
        imagePieces[r][c].setImageDrawable(finalDrawable);
    }
}