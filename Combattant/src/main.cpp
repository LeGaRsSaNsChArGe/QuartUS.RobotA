/*===========================================================
Plan du programme Arduino pour l’Épreuve du Combattant
=============================================================
ENTRÉES ET SORTIES ANALOGIQUES
Type      Nom                   Unité             Quantité
Entrée	  Suiveur de ligne	    Volts             1
          Sifflet	              Volts	            1
	        Capteur de couleur	  Volts	            1
	        Capteur de distance	  Volts             3
Sorties	  Moteur	              Vitesse -1 à 1    2
	        DEL	                  Signal            4
	        Angle du bras	        Volts	            1
===========================================================*/
#include <Arduino.h>
#include <LibRobus.h>

#define GAUCHE 0
#define DROITE 1

#define BLANC -2
#define GRIS -1
#define NOIR 0
#define BLEU 1
#define VERT 2
#define JAUNE 3
#define ROUGE 4

#define TEMPS_CYCLE 50

bool signal_sifflet = false;
int v_voulue = 0.5;
int tour = 0;
int couleur, couleur_voulue;

unsigned long t_actuel_cycle = 0;

float VitesseCorrigee(float v_voulue);
int CapteurCouleur();
void cycle();

void setup()
{
  BoardInit();
  
  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);

  couleur_voulue = CapteurCouleur(); //Vérification de la couleur au sol au moment du setup, soit vert ou jaune.
}

void loop()
{

  //Vérification du signal de sifflet
  //SI le signal correspond à 5 kHz pendant 2 secondes ALORS signal_sifflet := true

  if(signal_sifflet && (millis() - t_actuel_cycle > TEMPS_CYCLE))
  {
    t_actuel_cycle = millis();
    cycle();
  }
}





float VitesseCorrigee(float v_voulue)
{
  //FONCTION de PID à Charles ou Antoine...
  return 0/*retourne la vitesse corrigée*/;
}

int CapteurCouleur()
{
  int couleur;
  /*Capte la couleur
  blanc = BLANC
  noir = NOIR
  bleu = BLEU
  vert = VERT
  jaune = JAUNE
  rouge = ROUGE
  */

  return couleur;
}

void cycle()
{
  couleur = CapteurCouleur(); //Vérification de la couleur ou du blanc

  if(tour == 0 || tour == 1) //1e tour
  {
    if(couleur != BLANC && couleur != NOIR && couleur != GRIS)
    {
      switch(couleur_voulue - couleur)
      {
        case 0 :
          MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(v_voulue));
          MOTOR_SetSpeed(DROITE, VitesseCorrigee(v_voulue));
          break;
        case 1 :
        case 2 :
          MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(v_voulue*1.2));
          MOTOR_SetSpeed(DROITE, VitesseCorrigee(v_voulue*0.8));
          break;
        case -1 :
        case -2 :
          MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(v_voulue*0.8));
          MOTOR_SetSpeed(DROITE, VitesseCorrigee(v_voulue*1.2));
          break;
        default:
          break;
      }
    }
    else if(couleur == BLANC)
    {
      //Suiveur de ligne
    }
    else if(couleur == NOIR)
    {
      //Vous allez devoir faire un compteur qui compte le nombre de cycle de suite qui détecte du noir, tant que le noir est détecté, le tour ne change pas, quand le noir n'est plus détecté de suite, le tour augmente de 1.
      tour++;
    }
    else //GRIS
    {
      MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(v_voulue));
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(v_voulue));
    }
  }
  else //2e tour
  {
    //Tour avec raccourcis bleu et couleur jaune.
  }
}