package com.example.interfacequartus.Fragment;

import static com.example.interfacequartus.Activity.Accueil.activitePrecedente;
import static com.example.interfacequartus.Activity.Accueil.bluetooth;
import static com.example.interfacequartus.Activity.Partie.robotPret;
import static com.example.interfacequartus.Model.Quarto.ERREUR;
import static com.example.interfacequartus.Model.Quarto.ERREUR_SELECTION;
import static com.example.interfacequartus.Model.Quarto.OK;
import static com.example.interfacequartus.Model.Quarto.PLANCHE;
import static com.example.interfacequartus.Model.Quarto.VICTOIRE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.interfacequartus.Activity.Accueil;
import com.example.interfacequartus.Activity.Partie;
import com.example.interfacequartus.Activity.Regles;
import com.example.interfacequartus.Model.Case;
import com.example.interfacequartus.Model.Joueur;
import com.example.interfacequartus.R;
import com.example.interfacequartus.RelativeLayoutCarrePortrait;

public class Planche extends Fragment
{
    Partie parent;
    ImageView[][] imagePlanche = new ImageView[4][4];

    //TODO toute la mécanique du plateau

    public Planche()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_planche, container, false);

        parent = (Partie) requireActivity();

        parent.outils.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.aide:

                    for(int r = 0 ; r < 4 ; r++)
                        for(int c = 0 ; c < 4 ; c++)
                            //TODO suggestion poser
                            /*if(parent.getPartie().poserPieceEstSuggestion(r, c) && isVisible())
                                setSuggestion(r, c);*/
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
                RelativeLayoutCarrePortrait casePlanche = view.findViewById(getResources().getIdentifier("c" + r + c, "id", getActivity().getPackageName()));
                imagePlanche[r][c] = view.findViewById(getResources().getIdentifier("i" + r + c, "id", getActivity().getPackageName()));
                TextView textCase = view.findViewById(getResources().getIdentifier("t" + r + c, "id", getActivity().getPackageName()));

                Case t_case = parent.getPartie().getCase(PLANCHE, r, c);
                if(!t_case.estVide())
                {
                    imagePlanche[r][c].setColorFilter(ContextCompat.getColor(parent.getApplicationContext(), R.color.pieceCasePlanche), PorterDuff.Mode.SRC_OVER);
                    //imagePlanche[r][c].setImageDrawable(t_case.getPiece().getDrawable());
                    textCase.setText("T" + parent.getPartie().getCase(PLANCHE, r, c).getTour() + "\nJ" + parent.getPartie().getCase(PLANCHE, r, c).getJoueur());
                }

                int t_r = r;
                int t_c = c;
                casePlanche.setOnClickListener(view1 ->
                {
                    Toast message;
                    switch(parent.getPartie().poseSelection(t_r, t_c))
                    {
                        case OK:
                            imagePlanche[t_r][t_c].setColorFilter(ContextCompat.getColor(parent.getApplicationContext(), R.color.pieceCasePlanche), PorterDuff.Mode.SRC_OVER);
                            //imagePlanche[t_r][t_c].setImageDrawable(parent.getPartie().getCase(PLANCHE, t_r, t_c).getPiece().getDrawable());
                            textCase.setText("T" + parent.getPartie().getCase(PLANCHE, t_r, t_c).getTour() + "\nJ" + parent.getPartie().getCase(PLANCHE, t_r, t_c).getJoueur());

                            //TODO Bluetooth
                            if(parent.bluetoothActif())
                            {
                                robotPret = false;
                                parent.setBluetoothActif(bluetooth.envoieDonnees(parent.getIDplusplus(), 1, t_r, t_c, 0, parent.getPartie().getJoueurActif(), getFragmentManager()));
                                if(!parent.bluetoothActif())
                                {
                                    message = Toast.makeText(getContext(),"ERREUR de transmission Bluetooth\nBluetooth désactivé!.", Toast.LENGTH_SHORT);
                                    message.setGravity(Gravity.CENTER, 0, 0);
                                    message.show();
                                }
                            }
                            break;
                        case VICTOIRE:
                            imagePlanche[t_r][t_c].setColorFilter(ContextCompat.getColor(parent.getApplicationContext(), R.color.pieceCasePlanche), PorterDuff.Mode.SRC_OVER);
                            //imageCase.setImageDrawable(partie.getCase(PLANCHE, t_r, t_c).getPiece().getDrawable());
                            textCase.setText("T" + parent.getPartie().getCase(PLANCHE, t_r, t_c).getTour() + "\nJ" + parent.getPartie().getCase(PLANCHE, t_r, t_c).getJoueur());

                            //TODO Bluetooth
                            if(parent.bluetoothActif())
                            {
                                parent.setBluetoothActif(bluetooth.envoieDonnees(parent.getIDplusplus(), 1, t_r, t_c, 1, parent.getPartie().getJoueurActif(), getFragmentManager()));
                                if(!parent.bluetoothActif())
                                {
                                    message = Toast.makeText(getContext(),"ERREUR de transmission Bluetooth\nBluetooth désactivé!.", Toast.LENGTH_SHORT);
                                    message.setGravity(Gravity.CENTER, 0, 0);
                                    message.show();
                                }

                                /*if(trueTODO si signal recu du bluetooth)
                                {
                                    //TODO : envoie du signal du joueur vainceur
                                    if(parent.getPartie().getJoueur1().getTour() == Joueur.Tour.Actif)
                                    {
                                        bluetooth.envoieDonnees("#=1"); //Victoire joueur 1
                                    }
                                    else
                                    {
                                        if(parent.getPartie().getNiveau() > 0)
                                        {
                                            bluetooth.envoieDonnees("#=0"); //Victoire de QuartUS
                                        }
                                        else
                                            bluetooth.envoieDonnees("#=2"); //Victoire joueur 2
                                    }
                                }*/
                            }

                            if(parent.getPartie().getJoueur1().getTour() == Joueur.Tour.Actif)
                            {
                                if(parent.getPartie().getNiveau() > 0)
                                    Accueil.score[0]++;

                                message = Toast.makeText(getContext(), "Victoire du joueur 1", Toast.LENGTH_SHORT);
                            }
                            else
                            {
                                if(parent.getPartie().getNiveau() > 0)
                                {
                                    Accueil.score[1]++;
                                    message = Toast.makeText(getContext(), "Victoire de QuartUS!!!", Toast.LENGTH_SHORT);
                                }
                                else
                                    message = Toast.makeText(getContext(), "Victoire du joueur 2", Toast.LENGTH_SHORT);
                            }
                            message.setGravity(Gravity.CENTER, 0, 0);
                            message.show();

                            activitePrecedente = "Partie";

                            getActivity().finish();
                            break;
                        case ERREUR:
                            message = Toast.makeText(getContext(),"Case pleine...", Toast.LENGTH_SHORT);
                            message.setGravity(Gravity.CENTER, 0, 0);
                            message.show();
                            break;
                        case ERREUR_SELECTION:
                            message = Toast.makeText(getContext(),"Aucune pièce sélectionnée...", Toast.LENGTH_SHORT);
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
    public void setSuggestion(int r, int c)
    {
        imagePlanche[r][c].setColorFilter(ContextCompat.getColor(getContext(), R.color.pieceCasePlanche), PorterDuff.Mode.SRC_OVER);
    }
}