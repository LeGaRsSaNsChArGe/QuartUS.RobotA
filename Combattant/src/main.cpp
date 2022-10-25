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

//Librairies-------------------------------------------------
#include <Arduino.h>
#include <LibRobus.h>

//Constantes-------------------------------------------------
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

#define V_VOULUE 0.5

//Variables---------------------------------------------------
bool signal_sifflet = false;
int tour = 0; //Avant de traverser la ligne de départ
int couleur, couleur_voulue;
String etat_suiveurLigne;

unsigned long t_actuel_cycle = 0;
int suite_cycles_identiques = 0; //le nombre de cycles de suite qui sont identiques.

//Déclarations des fonctions----------------------------------
float VitesseCorrigee(float v_voulue);
int CapteurCouleur();
String CalculSuiveurLigne();
void cycle();

//Démarrage---------------------------------------------------
void setup()
{
  BoardInit();
  
  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);

  couleur_voulue = CapteurCouleur(); //Vérification de la couleur au sol au moment du setup, soit vert ou jaune.
}

//Main--------------------------------------------------------
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

//Définitions des fonctions-----------------------------------
float VitesseCorrigee(float v_voulue)
{
  //FONCTION de PID à Charles ou Antoine...
  return 0/*retourne la vitesse corrigée*/;
}

int CapteurCouleur()
{
  int couleur;
  /*
  Capte la couleur

  blanc = BLANC
  noir = NOIR
  bleu = BLEU
  vert = VERT
  jaune = JAUNE
  rouge = ROUGE
  */

  return couleur;
}

String CalculSuiveurLigne()
{
  String etat;

  float voltage_recu;//Variable de voltage reçu du suiveur de ligne

  //Changement de voltage_recu en fonction du voltage reçu du suiveur de ligne
  //Serait-il utile de mettre un compteur de cycles de suite qu'un état est détecté avant de retourner l'état ? À vous de voir en testant. Demandez-moi si vous avez besoin d'aide (Seb)

  /*
  G : détection de noir à gauche du robot
  C : détection de noir au centre du robot
  D : détection de noir à droite du robot
  */

  if(voltage_recu > 0/*Entre tant et plus*/)
    etat = "GCD";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "GC-";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "G-D";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "-CD";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "G--";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "-C-";
  else if(6 > voltage_recu > 0/*Entre tant et tant*/)
    etat = "--D";
  else
    etat = "---";

  return etat;
}

void cycle()
{
  couleur = CapteurCouleur(); //Vérification de la couleur ou du blanc
  etat_suiveurLigne = CalculSuiveurLigne(); //Vérification de l'état du suiveur de ligne


  if(etat_suiveurLigne == "GCD" && couleur != NOIR && couleur != GRIS) //Zone de couleur
  {
    switch(couleur_voulue - couleur)
    {
      case 0 :
        MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE));
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE));
        break;
      case 1 :
      case 2 :
        MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE*1.2));
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE*0.8));
        break;
      case -1 :
      case -2 :
        MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE*0.8));
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE*1.2));
        break;
      default:
        break;
    }
  }
  else if(etat_suiveurLigne != "GCD") //Zone du suiveur de ligne : un des capteurs ne détecte plus de noir
  {
    if(tour == 1)
    {
      //Suiveur de ligne SI la tension du suiveur de ligne droit capte du blanc. SINON, On avance le robot tant qu'une couleur autre que Blanc, Noir ou gris soit détectée.
    }
    else if(tour == 2)
    {
      //Tournant d'un angle sur lui-même vers la zone bleue pendant pour un delay
      //Couleur voulue devient bleu
    }
  }
  else if(couleur == NOIR) //Ligne de départ/d'arrivée
  {
    //Vous allez devoir faire un compteur qui compte le nombre de cycle de suite qui détecte du noir, tant que le noir est détecté, le tour ne change pas, quand le noir n'est plus détecté de suite, le tour augmente de 1.
    if(tour++ == 2) //tour++ augmente le tour
      couleur_voulue = JAUNE; //Si c'est le 2e tour, couleur voulue = jaune.
    
    MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE));
    MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE));
  }
  else //Zone grise ou jump
  {
    MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE));
    MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE));
  }
}