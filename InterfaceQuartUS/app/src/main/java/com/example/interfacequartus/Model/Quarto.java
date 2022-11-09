package com.example.interfacequartus.Model;

import android.content.Context;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.interfacequartus.BuildConfig;
import com.example.interfacequartus.Model.Piece.Couleur;
import com.example.interfacequartus.Model.Piece.Forme;
import com.example.interfacequartus.Model.Piece.Taille;
import com.example.interfacequartus.Model.Piece.Remplissage;

public class Quarto
{
    //Constantes
    public static final int OK = 0;
    public static final int ERREUR = 1;
    public static final int ERREUR_SELECTION = 2;
    public static final int ERREUR_VIDE = 3;

    public static final int HUMAIN = 0;

    public static final int SIMPLE = 0;
    public static final int CLASSIQUE = 1;
    public static final int COMPLEXE = 2;

    //Variables
    Context context;

    Joueur[] joueurs;
    int niveau, mode;

    Case[][] planche, pieces;
    Piece selection;

    //Constructeurs
    public Quarto(Context context, int niveau, int mode)
    {
        this.context = context;

        this.niveau = niveau;
        this.mode = mode;

        switch(niveau)
        {
            case HUMAIN:
                this.joueurs[0] = new Joueur();
                this.joueurs[1] = new Joueur();
                break;
            case 1:
                //TODO
                break;
            case 2:
                //TODO
                break;
            case 3:
                //TODO
                break;
            default:
                break;
        }

        this.planche = new Case[4][4];
        this.pieces = new Case[4][4];
        this.selection = new Piece();

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
    public Case[][] getPlanche()
    {
        return planche;
    }
    public Case[][] getPieces()
    {
        return pieces;
    }
    public Piece getSelection()
    {
        return selection;
    }

    public void setJoueur1(Joueur j)
    {
        this.joueurs[0] = j;
    }
    public void setJoueur2(Joueur j)
    {
        this.joueurs[1] = j;
    }
    public void setPlanche(Case[][] planche)
    {
        this.planche = planche;
    }
    public void setPieces(Case[][] pieces)
    {
        this.pieces = pieces;
    }
    public void setSelection(Piece selection)
    {
        this.selection = selection;
    }

    //Méthodes
    public int prendreSelection(int x, int y)
    {
        if(this.pieces[x][y] == null)
            return ERREUR_VIDE;
        else if(this.selection != null)
            return ERREUR_SELECTION;

        this.selection = this.pieces[x][y].getPiece();
        this.pieces[x][y] = new Case();

        return OK;
    }
    public int poseSelection(int x, int y)
    {
        if(this.planche[x][y] != null)
            return ERREUR;
        else if(this.selection == null)
            return ERREUR_SELECTION;

        this.planche[x][y] = new Case(this.selection);
        this.selection = null;

        return OK;
    }

    public boolean victoire()
    {
        boolean resultat = false;
        Case[] ligne = new Case[4];

        //Lignes
        for(int r = 0 ; r < 4 ; r++)
        {
            for(int c = 0 ; c < 4 ; c++)
                ligne[c] = this.planche[r][c];

            resultat = resultat | verificationLigne(ligne);
        }

        //Colonnes
        for(int c = 0 ; c < 4 ; c++)
        {
            for(int r = 0 ; r < 4 ; r++)
                ligne[r] = this.planche[r][c];

            resultat = resultat | verificationLigne(ligne);
        }

        //diagonales
        for(int r = 0 ; r < 4 ; r++)
            ligne[r] = this.planche[r][r];

        resultat = resultat | verificationLigne(ligne);

        for(int r = 0 ; r < 4 ; r++)
            ligne[r] = this.planche[r][3 - r];

        resultat = resultat | verificationLigne(ligne);

        if(mode == COMPLEXE)
        {
            for(int r = 0 ; r < 4 ; r += 2)
            {
                for(int c = 0 ; c < 4 ; c += 2)
                {
                    ligne[0] = this.planche[r][c];
                    ligne[1] = this.planche[r][c+1];
                    ligne[2] = this.planche[r+1][c];
                    ligne[3] = this.planche[r+1][c+1];

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
        else if(ligne[0].getPiece().getTaille() == ligne[1].getPiece().getTaille()
                & ligne[1].getPiece().getTaille() == ligne[2].getPiece().getTaille()
                & ligne[2].getPiece().getTaille() == ligne[3].getPiece().getTaille()) //Taille
            return true;
        else if(ligne[0].getPiece().getRemplissage() == ligne[1].getPiece().getRemplissage()
                & ligne[1].getPiece().getRemplissage() == ligne[2].getPiece().getRemplissage()
                & ligne[2].getPiece().getRemplissage() == ligne[3].getPiece().getRemplissage()) //Remplissage
            return true;
        else
            return false;
    }
}
