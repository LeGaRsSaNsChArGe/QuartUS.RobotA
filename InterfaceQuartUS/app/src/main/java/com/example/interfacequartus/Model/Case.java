package com.example.interfacequartus.Model;

import android.graphics.drawable.Drawable;

public class Case
{
    //Variables
    Piece piece;
    int[] coords = new int[2]; // (x, y)

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
    public int[] getCoords()
    {
        return coords;
    }
    public Piece getPiece()
    {
        return piece;
    }

    public void setCoords(int[] coords)
    {
        this.coords = coords;
    }
    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    //Méthodes
    public boolean estVide()
    {
        return this.piece == null;
    }
}
