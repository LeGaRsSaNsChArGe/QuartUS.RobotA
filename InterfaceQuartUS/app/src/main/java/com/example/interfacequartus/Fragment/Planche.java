package com.example.interfacequartus.Fragment;

import static com.example.interfacequartus.Activity.Accueil.DEBUG;
import static com.example.interfacequartus.Activity.Accueil.activitePrecedente;
import static com.example.interfacequartus.Activity.Accueil.bluetooth;
import static com.example.interfacequartus.Model.Quarto.EGALITE;
import static com.example.interfacequartus.Model.Quarto.ERREUR;
import static com.example.interfacequartus.Model.Quarto.ERREUR_CONFIRMATION;
import static com.example.interfacequartus.Model.Quarto.ERREUR_SELECTION;
import static com.example.interfacequartus.Model.Quarto.OK;
import static com.example.interfacequartus.Model.Quarto.PLANCHE;
import static com.example.interfacequartus.Model.Quarto.POSER_PIECE;
import static com.example.interfacequartus.Model.Quarto.VICTOIRE;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.interfacequartus.Activity.Accueil;
import com.example.interfacequartus.Activity.Partie;
import com.example.interfacequartus.Activity.Regles;
import com.example.interfacequartus.BuildConfig;
import com.example.interfacequartus.Model.Case;
import com.example.interfacequartus.Model.Joueur;
import com.example.interfacequartus.R;
import com.example.interfacequartus.RelativeLayoutCarrePortrait;

public class Planche extends Fragment
{
    Partie parent;
    ImageView[][] imagePlanche = new ImageView[4][4];
    TextView[][] textCases = new TextView[4][4];

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
                    {
                        for (int c = 0; c < 4; c++)
                        {
                            int indice = parent.getPartie().indiceSuggestionPlanche(r, c);
                            if (indice != -1 && isVisible())
                            {
                                this.textCases[r][c].setText("" + indice);
                                this.textCases[r][c].setTextColor(getResources().getColor(R.color.black));
                                this.textCases[r][c].setTextSize(50);
                            }
                            else if(indice == -1)
                            {
                                this.textCases[r][c].setText("");
                                this.textCases[r][c].setTextSize(50);
                            }
                        }
                    }
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
                textCases[r][c] = view.findViewById(getResources().getIdentifier("t" + r + c, "id", getActivity().getPackageName()));

                miseAJourCase(textCases[r][c], r, c);

                int t_r = r;
                int t_c = c;
                casePlanche.setOnClickListener(view1 ->
                {
                    Toast message;
                    int cas = ERREUR_CONFIRMATION;
                    if(bluetooth.estConfirmationFinEtape() && bluetooth.estActif())
                        cas = parent.getPartie().poseSelection(t_r, t_c);
                    else if(!bluetooth.estActif())
                        cas = parent.getPartie().poseSelection(t_r, t_c);

                    switch(cas)
                    {
                        case OK:
                            miseAJourCase(textCases[t_r][t_c], t_r, t_c);

                            if(bluetooth.estActif())
                            {
                                bluetooth.setActif(bluetooth.envoieDonnees(parent.getIDplusplus(), POSER_PIECE, t_r, t_c, OK, parent.getPartie().getJoueurActif()));
                                if(!bluetooth.estActif())
                                {
                                    message = Toast.makeText(getContext(),"ERREUR de transmission Bluetooth\nBluetooth désactivé!.", Toast.LENGTH_SHORT);
                                    message.show();
                                }
                            }
                            break;
                        case VICTOIRE:
                        case EGALITE:
                            miseAJourCase(textCases[t_r][t_c], t_r, t_c);

                            if(bluetooth.estActif())
                            {
                                if(cas == VICTOIRE)
                                    bluetooth.setActif(bluetooth.envoieDonnees(parent.getIDplusplus(), POSER_PIECE, t_r, t_c, VICTOIRE, parent.getPartie().getJoueurActif()));
                                else
                                    bluetooth.setActif(bluetooth.envoieDonnees(parent.getIDplusplus(), POSER_PIECE, t_r, t_c, EGALITE, parent.getPartie().getJoueurActif()));

                                if(!bluetooth.estActif())
                                {
                                    message = Toast.makeText(getContext(),"ERREUR de transmission Bluetooth\nBluetooth désactivé!.", Toast.LENGTH_SHORT);
                                    message.show();
                                }
                            }

                            if(cas == VICTOIRE)
                            {
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
                            }
                            else
                                message = Toast.makeText(getContext(), "Égalité entre les joueurs!", Toast.LENGTH_SHORT);

                            message.show();

                            activitePrecedente = "Partie";

                            getActivity().finish();
                            break;
                        case ERREUR:
                            message = Toast.makeText(getContext(),"Case pleine...", Toast.LENGTH_SHORT);
                            message.show();
                            break;
                        case ERREUR_SELECTION:
                            message = Toast.makeText(getContext(),"Aucune pièce sélectionnée...", Toast.LENGTH_SHORT);
                            message.show();
                            break;
                        case ERREUR_CONFIRMATION:
                            message = Toast.makeText(getContext(),"Robot en déplacement, attendez...", Toast.LENGTH_SHORT);
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
        //imagePlanche[r][c].setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu), PorterDuff.Mode.SRC_OVER);
    }

    private void miseAJourCase(TextView textCase, int r, int c)
    {
        for(int r1 = 0 ; r1 < 4 ; r1++)
        {
            for (int c1 = 0; c1 < 4; c1++)
            {
                if(this.textCases[r1][c1] != null)
                {
                    this.textCases[r1][c1].setText("\n");
                    this.textCases[r1][c1].setTextSize(24);
                }
            }
        }


        Case t_case = parent.getPartie().getCase(PLANCHE, r, c);
        if(!t_case.estVide())
        {
            if(bluetooth.estActif())
            {
                imagePlanche[r][c].setColorFilter(ContextCompat.getColor(parent.getApplicationContext(), R.color.pieceCasePlanche), PorterDuff.Mode.SRC_OVER);
                textCase.setText("T" + parent.getPartie().getCase(PLANCHE, r, c).getTour() + "\nJ" + parent.getPartie().getCase(PLANCHE, r, c).getJoueur());
            }
            else
            {
                imagePlanche[r][c].setImageDrawable(t_case.getPiece().getDrawable());
            }
        }
    }
}