package com.example.interfacequartus.Model;

public class Case
{
    //Variables
    Piece piece;
    int tour;
    int joueur;

    //Constructeurs
    public Case()
    {
        this.piece = null;
    }
    public Case(Piece piece)
    {
        this.piece = piece;
    }

    //Getteurs & setteurs
    public Piece getPiece()
    {
        return piece;
    }
    public int getTour()
    {
        return tour;
    }
    public int getJoueur()
    {
        return joueur;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }
    public void setTour(int tour)
    {
        this.tour = tour;
    }
    public void setJoueur(int joueur)
    {
        this.joueur = joueur;
    }

    //Méthodes
    public boolean estVide()
    {
        return this.piece == null;
    }
}
