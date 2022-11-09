package com.example.interfacequartus.Model;

public class Joueur
{
    //Variables
    public enum Tour{Actif, Passif} private Tour tour;

    //Constructeurs
    public Joueur()
    {
        this.tour = Tour.Passif;
    }

    //Getteurs & setteurs
    public Tour getTour()
    {
        return tour;
    }

    public void setTour(Tour tour)
    {
        this.tour = tour;
    }
}
