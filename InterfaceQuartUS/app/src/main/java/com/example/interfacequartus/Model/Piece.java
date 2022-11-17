package com.example.interfacequartus.Model;

import android.graphics.drawable.Drawable;

public class Piece
{
    //Variables
    public enum Couleur{Blanche, Noire} private Couleur couleur;
    public enum Forme{Ronde, Carre} private Forme forme;
    public enum Taille{Haute, Basse} private Taille taille;
    public enum Remplissage{Pleine, Creuse} private Remplissage remplissage;

    Drawable drawable;

    //Constructeurs
    public Piece()
    {

    }
    public Piece(Couleur couleur, Forme forme, Taille taille, Remplissage remplissage)
    {
        this.couleur = couleur;
        this.forme = forme;
        this.taille = taille;
        this.remplissage = remplissage;
    }

    //Getteurs & setteurs
    public Couleur getCouleur()
    {
        return couleur;
    }
    public Forme getForme()
    {
        return forme;
    }
    public Taille getTaille()
    {
        return taille;
    }
    public Remplissage getRemplissage()
    {
        return remplissage;
    }
    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setCouleur(Couleur couleur)
    {
        this.couleur = couleur;
    }
    public void setForme(Forme forme)
    {
        this.forme = forme;
    }
    public void setTaille(Taille taille)
    {
        this.taille = taille;
    }
    public void setRemplissage(Remplissage remplissage)
    {
        this.remplissage = remplissage;
    }
    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }

    //Méthodes
    public String getCaracteristiques()
    {
        String caracteristiques = "";
        switch (this.couleur)
        {
            case Blanche:
                caracteristiques += "JAUNE";
                break;
            case Noire:
                caracteristiques += "VERT";
                break;
            default:
                break;
        }
        caracteristiques += "/";
        switch (this.forme)
        {
            case Carre:
                caracteristiques += "CARRÉE";
                break;
            case Ronde:
                caracteristiques += "RONDE";
                break;
            default:
                break;
        }
        caracteristiques += "/";
        switch (this.taille)
        {
            case Haute:
                caracteristiques += "HAUTE";
                break;
            case Basse:
                caracteristiques += "BASSE";
                break;
            default:
                break;
        }
        caracteristiques += "/";
        switch (this.remplissage)
        {
            case Pleine:
                caracteristiques += "PLEINE";
                break;
            case Creuse:
                caracteristiques += "CREUSE";
                break;
            default:
                break;
        }
        return caracteristiques;
    }
}
