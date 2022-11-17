package com.example.interfacequartus.Model;

public class JoueurAI extends Joueur
{
    //Variables
    int niveau;

    //Constructeurs
    public JoueurAI(Tour tour, int niveau)
    {
        super(tour);

        this.niveau = niveau;
    }

    //Getteurs & setteurs
    public int getNiveau()
    {
        return niveau;
    }

    public void setNiveau(int niveau)
    {
        this.niveau = niveau;
    }

    //Méthodes

}
