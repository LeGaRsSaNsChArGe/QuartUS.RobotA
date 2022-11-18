package com.example.interfacequartus.Model;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.interfacequartus.BuildConfig;
import com.example.interfacequartus.Model.Piece.Couleur;
import com.example.interfacequartus.Model.Piece.Forme;
import com.example.interfacequartus.Model.Piece.Taille;
import com.example.interfacequartus.Model.Piece.Remplissage;

import static com.example.interfacequartus.Activity.Accueil.DEBUG;
import static com.example.interfacequartus.Activity.Partie.robotPret;

public class Quarto
{
    //Constantes
    public static final int EGALITE = -2;
    public static final int VICTOIRE = -1;
    public static final int OK = 0;
    public static final int ERREUR = 1;
    public static final int ERREUR_SELECTION = 2;
    public static final int ERREUR_VIDE = 3;
    public static final int ERREUR_ROBOT_NON_PRET = 4;

    public static final int HUMAIN = 0;

    public static final int SIMPLE = 0;
    public static final int CLASSIQUE = 1;
    public static final int COMPLEXE = 2;

    public static final int PLANCHE = 0;
    public static final int PIECES = 1;

    //Variables
    Context context;

    Joueur[] joueurs;
    int tour;
    int niveau, mode;

    Case[][] planche, pieces;
    Piece selection;

    //Constructeurs
    public Quarto(Context context, int niveau, int mode)
    {
        this.context = context;

        this.niveau = niveau;
        this.mode = mode;

        this.joueurs = new Joueur[2];
        this.tour = 0;

        switch(niveau)
        {
            case HUMAIN:
                this.joueurs[0] = new Joueur(Joueur.Tour.Passif);
                this.joueurs[1] = new Joueur(Joueur.Tour.Actif);
                break;
            case 1:
            case 2:
            case 3:
                this.joueurs[0] = new Joueur(Joueur.Tour.Passif);
                this.joueurs[1] = new JoueurAI(Joueur.Tour.Actif, niveau);
                break;
            default:
                break;
        }

        this.planche = new Case[4][4];
        this.pieces = new Case[4][4];
        this.selection = null;

        for(int r = 0 ; r < 4 ; r++)
            for(int c = 0 ; c < 4 ; c++)
                this.planche[r][c] = new Case();

        //rangée 0
        this.pieces[0][0] = new Case(new Piece(Couleur.Noire, Forme.Carre, Taille.Basse, Remplissage.Pleine)); //Colonne 0
        this.pieces[0][1] = new Case(new Piece(Couleur.Noire, Forme.Carre, Taille.Basse, Remplissage.Creuse)); //Colonne 1
        this.pieces[0][2] = new Case(new Piece(Couleur.Noire, Forme.Ronde, Taille.Basse, Remplissage.Pleine)); //Colonne 2
        this.pieces[0][3] = new Case(new Piece(Couleur.Noire, Forme.Ronde, Taille.Basse, Remplissage.Creuse)); //Colonne 3

        //rangée 1
        this.pieces[1][0] = new Case(new Piece(Couleur.Noire, Forme.Carre, Taille.Haute, Remplissage.Pleine)); //Colonne 0
        this.pieces[1][1] = new Case(new Piece(Couleur.Noire, Forme.Carre, Taille.Haute, Remplissage.Creuse)); //Colonne 1
        this.pieces[1][2] = new Case(new Piece(Couleur.Noire, Forme.Ronde, Taille.Haute, Remplissage.Pleine)); //Colonne 2
        this.pieces[1][3] = new Case(new Piece(Couleur.Noire, Forme.Ronde, Taille.Haute, Remplissage.Creuse)); //Colonne 3

        //rangée 2
        this.pieces[2][0] = new Case(new Piece(Couleur.Blanche, Forme.Carre, Taille.Basse, Remplissage.Pleine)); //Colonne 0
        this.pieces[2][1] = new Case(new Piece(Couleur.Blanche, Forme.Carre, Taille.Basse, Remplissage.Creuse)); //Colonne 1
        this.pieces[2][2] = new Case(new Piece(Couleur.Blanche, Forme.Ronde, Taille.Basse, Remplissage.Pleine)); //Colonne 2
        this.pieces[2][3] = new Case(new Piece(Couleur.Blanche, Forme.Ronde, Taille.Basse, Remplissage.Creuse)); //Colonne 3

        //rangée 3
        this.pieces[3][0] = new Case(new Piece(Couleur.Blanche, Forme.Carre, Taille.Haute, Remplissage.Pleine)); //Colonne 0
        this.pieces[3][1] = new Case(new Piece(Couleur.Blanche, Forme.Carre, Taille.Haute, Remplissage.Creuse)); //Colonne 1
        this.pieces[3][2] = new Case(new Piece(Couleur.Blanche, Forme.Ronde, Taille.Haute, Remplissage.Pleine)); //Colonne 2
        this.pieces[3][3] = new Case(new Piece(Couleur.Blanche, Forme.Ronde, Taille.Haute, Remplissage.Creuse)); //Colonne 3

        for(int r = 0 ; r < 4 ; r++)
            for(int c = 0 ; c < 4 ; c++)
                this.pieces[r][c].getPiece().setDrawable(AppCompatResources.getDrawable(context, context.getResources().getIdentifier("p_" + r + c, "drawable", BuildConfig.APPLICATION_ID)));
    }

    //Getteurs & setteurs
    public Joueur getJoueur1()
    {
        return joueurs[0];
    }
    public Joueur getJoueur2()
    {
        return joueurs[1];
    }
    public int getTour() {
        return tour;
    }
    public Case[][] getPlanche()
    {
        return planche;
    }
    public Case[][] getPieces()
    {
        return pieces;
    }
    public Case getCase(int lieu,int r, int c)
    {
        if(lieu == PLANCHE)
            return this.planche[r][c];
        else if(lieu == PIECES)
            return this.pieces[r][c];

        return null;
    }
    public Piece getSelection()
    {
        return selection;
    }
    public int getNiveau()
    {
        return niveau;
    }
    public int getJoueurActif()
    {
        if(this.joueurs[0].getTour() == Joueur.Tour.Actif)
            return 1;
        else
            return 2;
    }

    public void setJoueur1(Joueur j)
    {
        this.joueurs[0] = j;
    }
    public void setJoueur2(Joueur j)
    {
        this.joueurs[1] = j;
    }
    public void setTour(int tour) {
        this.tour = tour;
    }
    public void setPlanche(Case[][] planche)
    {
        this.planche = planche;
    }
    public void setPieces(Case[][] pieces)
    {
        this.pieces = pieces;
    }
    public void setCase(Case t_case, int lieu,int r, int c)
    {
        if(lieu == PLANCHE)
            this.planche[r][c] = t_case;
        else if(lieu == PIECES)
            this.pieces[r][c] = t_case;
    }
    public void setSelection(Piece selection)
    {
        this.selection = selection;
    }

    //Méthodes
    public int prendreSelection(int x, int y)
    {
        if(robotPret == false)
            return ERREUR_ROBOT_NON_PRET;
        if(this.pieces[x][y].getPiece() == null)
            return ERREUR_VIDE;
        else if(this.selection != null)
            return ERREUR_SELECTION;




        this.selection = this.pieces[x][y].getPiece();
        this.pieces[x][y] = new Case();

        if(joueurs[0].getTour() == Joueur.Tour.Actif)
        {
            joueurs[0].setTour(Joueur.Tour.Passif);
            joueurs[1].setTour(Joueur.Tour.Actif);
        }
        else if(joueurs[1].getTour() == Joueur.Tour.Actif)
        {
            joueurs[0].setTour(Joueur.Tour.Actif);
            joueurs[1].setTour(Joueur.Tour.Passif);
        }
        this.tour++;

        return OK;
    }
    public int poseSelection(int x, int y)
    {
        Log.d(DEBUG, String.valueOf(robotPret));
        if(robotPret == false)
            return ERREUR_ROBOT_NON_PRET;
        if(this.planche[x][y].getPiece() != null)
            return ERREUR;
        else if(this.selection == null)
            return ERREUR_SELECTION;


        this.planche[x][y] = new Case(this.selection);
        this.selection = null;

        this.planche[x][y].setJoueur(getJoueurActif());
        this.planche[x][y].setTour(tour);

        if(victoire(null))
            return VICTOIRE;

        //TODO égalité

        return OK;
    }

    public boolean victoire(Case[][] planche)
    {
        boolean resultat = false;
        Case[] ligne = new Case[4];

        if(planche == null)
        {
            planche = new Case[4][4];
            for(int r = 0 ; r < 4 ; r++)
            {
                for (int c = 0 ; c < 4 ; c++)
                {
                    planche[r][c] = this.planche[r][c];
                }
            }
        }

        //Rangées
        for(int r = 0 ; r < 4 ; r++)
        {
            for(int c = 0 ; c < 4 ; c++)
                ligne[c] = planche[r][c];

            resultat = resultat | verificationLigne(ligne);
        }

        //Colonnes
        for(int c = 0 ; c < 4 ; c++)
        {
            for(int r = 0 ; r < 4 ; r++)
                ligne[r] = planche[r][c];

            resultat = resultat | verificationLigne(ligne);
        }

        //diagonales
        for(int r = 0 ; r < 4 ; r++)
            ligne[r] = planche[r][r];

        resultat = resultat | verificationLigne(ligne);

        for(int r = 0 ; r < 4 ; r++)
            ligne[r] = planche[r][3 - r];

        resultat = resultat | verificationLigne(ligne);

        //Coins
        if(this.mode == COMPLEXE)
        {
            for(int r = 0 ; r < 4 ; r += 2)
            {
                for(int c = 0 ; c < 4 ; c += 2)
                {
                    ligne[0] = planche[r][c];
                    ligne[1] = planche[r][c+1];
                    ligne[2] = planche[r+1][c];
                    ligne[3] = planche[r+1][c+1];

                    resultat = resultat | verificationLigne(ligne);
                }
            }
        }

        return resultat;
    }

    public boolean verificationLigne(Case[] ligne)
    {
        //La ligne contient au moins une pièce vide
        if (ligne[0].getPiece() == null || ligne[1].getPiece() == null || ligne[2].getPiece() == null || ligne[3].getPiece() == null)
            return false;
        else if(ligne[0].getPiece().getCouleur() == ligne[1].getPiece().getCouleur()
                & ligne[1].getPiece().getCouleur() == ligne[2].getPiece().getCouleur()
                & ligne[2].getPiece().getCouleur() == ligne[3].getPiece().getCouleur()) //Couleur
            return true;
        else if(ligne[0].getPiece().getForme() == ligne[1].getPiece().getForme()
                & ligne[1].getPiece().getForme() == ligne[2].getPiece().getForme()
                & ligne[2].getPiece().getForme() == ligne[3].getPiece().getForme()) //Forme
            return true;
        else if(this.mode == CLASSIQUE &&
                (ligne[0].getPiece().getTaille() == ligne[1].getPiece().getTaille()
                & ligne[1].getPiece().getTaille() == ligne[2].getPiece().getTaille()
                & ligne[2].getPiece().getTaille() == ligne[3].getPiece().getTaille())) //Taille
            return true;
        else if(this.mode == CLASSIQUE &&
                (ligne[0].getPiece().getRemplissage() == ligne[1].getPiece().getRemplissage()
                & ligne[1].getPiece().getRemplissage() == ligne[2].getPiece().getRemplissage()
                & ligne[2].getPiece().getRemplissage() == ligne[3].getPiece().getRemplissage())) //Remplissage
            return true;
        else
            return false;
    }

    /*public boolean[][] suggestions()
    {
        boolean[][] suggestions = new boolean[4][4];

        for(int r = 0 ; r < 4 ; r++)
        {
            for(int c = 0 ; c < 4 ; c++)
            {
                if(this.planche[r][c].getPiece() == null && this.selection != null)
                {
                    //TODO Algorithme batard, à remplacer éventuellement
                    suggestions[r][c] = verificationVictoirePiece(this.selection, r, c);
                }
                else if(this.pieces[r][c].getPiece() != null && this.selection == null)
                {
                    suggestions[r][c] = !verificationVictoirePiece(this.pieces[r][c].getPiece(), r, c);
                }
            }
        }

        return suggestions;
    }*/

    public boolean prendrePieceEstSuggestion(int t_r, int t_c)
    {
        if(this.selection != null || this.pieces[t_r][t_c].estVide())
            return false;

        Case[][] planche = new Case[4][4];
        for(int r = 0 ; r < 4 ; r++)
            for(int c = 0 ; c < 4 ; c++)
                planche[r][c] = this.planche[r][c];

        for(int r = 0 ; r < 4 ; r++)
        {
            for (int c = 0 ; c < 4 ; c++)
            {
                if(planche[r][c].estVide())
                {
                    planche[r][c] = new Case(this.pieces[t_r][t_c].getPiece());
                    if(victoire(planche))
                        return false;

                    planche[r][c] = this.planche[r][c];
                }
            }
        }

        return true;
    }

    /*public boolean verificationVictoirePiece(Piece piece)
    {
        Case[][] planche = new Case[4][4];
        for(int r = 0 ; r < 4 ; r++)
            for(int c = 0 ; c < 4 ; c++)
                planche[r][c] = this.planche[r][c];

        for(int r = 0 ; r < 4 ; r++)
        {
            for (int c = 0 ; c < 4 ; c++)
            {
                planche[r][c].setPiece(piece);
                if(victoire(planche))
                    return true;

                planche[r][c] = this.planche[r][c];
            }
        }

        return false;
    }*/

    /*public boolean verificationVictoirePiece(Piece piece, int r_piece, int c_piece)
    {
        Case[] ligne = new Case[4];

        //Ligne
        for(int c = 0; c < 4; c++)
            ligne[c] = this.planche[r_piece][c];
        ligne[c_piece] = new Case(piece);

        if(verificationLigne(ligne))
            return true;

        //Colonne
        for(int r = 0; r < 4; r++)
            ligne[r] = this.planche[r][c_piece];
        ligne[r_piece] = new Case(piece);

        if(verificationLigne(ligne))
            return true;

        //diagonales
        if(r_piece == c_piece)
        {
            for(int r = 0; r < 4; r++)
                ligne[r] = this.planche[r][r];
            ligne[r_piece] = new Case(piece);

            if(verificationLigne(ligne))
                return true;
        }

        if(r_piece + c_piece == 3)
        {
            for(int r = 0; r < 4; r++)
                ligne[r] = this.planche[r][3 - r];
            ligne[r_piece] = new Case(piece);

            if(verificationLigne(ligne))
                return true;
        }

        //Coin
        if(this.mode == COMPLEXE)
        {
            for(int r = 0; r < 4; r += 2)
            {
                for(int c = 0; c < 4; c += 2)
                {
                    if(r+1 >= r_piece && r_piece >= r && c+1 >= c_piece && c_piece >= c)
                    {
                        ligne[0] = this.planche[r][c];
                        ligne[1] = this.planche[r][c + 1];
                        ligne[2] = this.planche[r + 1][c];
                        ligne[3] = this.planche[r + 1][c + 1];

                        if(r == r_piece && c == c_piece)
                            ligne[0] = new Case(piece);
                        else if(r == r_piece && (c+1) == c_piece)
                            ligne[1] = new Case(piece);
                        else if((r+1) == r_piece && c == c_piece)
                            ligne[2] = new Case(piece);
                        else if((r+1) == r_piece && (c+1) == c_piece)
                            ligne[3] = new Case(piece);

                        if(verificationLigne(ligne))
                            return true;
                    }
                }
            }
        }

        return false;
    }*/
}
