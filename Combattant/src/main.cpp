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
=============================================================
INSTRUCTIONS
=============================================================
Voici le plan! Veuillez compléter les sections marquées À FAIRE(x) dans l'ordre d'importance x où x = 1 étant le plus prioritaire.

N'hésitez pas à m'écrire ou m'appeler n'importe quand pour de l'aide, si vous vous demandez comment un bout de code fonctionne, s'il y a des bugs de code/de tests ou si j'ai oublié une partie :) Évidemment, évitez de modifier des bouts que j'ai déjà codé sans m'avoir consulté svp. Je serai plus réceptif à aider dans ces situations-là.

Si vous commencez à regarder le plan et que vous ne comprenez pas une partie, contactez-moi!

N'oubliez-pas de bien commentez votre code pour qu'on se repère facilement comme je l'ai fait pour vous svp. J'espère avoir assez détaillé mes commentaires XD

Criss de chat, arrête de m'gosser quand j'écris!

Aussi, il est important qu'on ait pomal la même méthodologie de code pour qu'on s'évite à tous d'avoir du code difforme, mais si vous ne voyez pas ce que je veux dire, je viendrai corriger à la toute fin haha.

Ayez des noms(Ex: nom de variable ou de fonction) complets pour qu'on puisse bien se repérer entre nous et pour ceux qui commencent à apprendre.

La partie du plan pour le bras n'a pas été faite étant donné qu'on a pas encore décidé comment on allait faire exactement avec le bras, alors quand vous serez rendu là, contactez-moi et je ferai le plan du bras.

-Seb
===========================================================*/

//Librairies-------------------------------------------------
#include <Arduino.h>
#include <LibRobus.h>

//Constantes-------------------------------------------------
#define GAUCHE 0
#define DROITE 1

#define DEPART -1
#define COULEUR 0
#define SUIVEUR_LIGNE 1
#define RACCOURCI 2 

#define BLANC -2
#define GRIS -1
#define NOIR 0
#define BLEU 1
#define VERT 2
#define JAUNE 3
#define ROUGE 4

#define TEMPS_CYCLE 50

#define V_VOULUE 0.5

//Variables globales------------------------------------------
bool signal_sifflet;
int tour; //Tour 1 ou 2
int couleur, couleur_voulue;
String etat_suiveurLigne;
int zone; //Zone de couleur, de raccourci ou de suiveur de ligne

unsigned long t_actuel_cycle = 0;
int suite_cycles_identiques = 0; //le nombre de cycles de suite qui sont identiques.

//Déclarations des fonctions----------------------------------
float VitesseCorrigee(float v_voulue); //PID de vitesse
int VerificationCouleur(); //Capteur de couleur
String VerificationSuiveurLigne(); // Suiveur de ligne
int VerificationZone(); //Vérifie si le robot est dans la zone de COULEUR, du SUIVEUR_LIGNE, du RACCOURCI ou s'il traverse la ligne de DEPART
void cycle(); //Le gros stock

//Démarrage---------------------------------------------------
void setup()
{
  BoardInit();
  
  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);

  signal_sifflet = false;
  tour = 0; //Avant de traverser la ligne de départ

  couleur_voulue = VerificationCouleur(); //Vérification de la couleur au sol au moment du setup, soit vert ou jaune.
}

//Main--------------------------------------------------------
void loop()
{

  //À FAIRE(1) : Vérification du signal de sifflet
  //SI le signal correspond à 5 kHz pendant 2 secondes ALORS signal_sifflet := true ET allume la DEL correspondante à la variable couleur_voulue.

  if(signal_sifflet && tour <= 2 && (millis() - t_actuel_cycle > TEMPS_CYCLE)) //Mettez en commentaire et faite votre propre code dans le loop si vous testez des sections ou des fonctions.
  {
    t_actuel_cycle = millis();
    cycle(); //Le cycle du robot à faire à chaque intervalle de TEMPS_CYCLE
  }
  else if(tour > 2)
  {
    setup(); //On reset le robot et les moteurs à 0.
  }
}

//Définitions des fonctions-----------------------------------
float VitesseCorrigee(float v_voulue)
{
  //À FAIRE(2) : FONCTION de PID à Charles ou Antoine...
  return 0/*retourne la vitesse corrigée*/;
}

int VerificationCouleur()
{
  int couleur;
  /*
  À FAIRE(3) : Capte la couleur

  blanc = BLANC
  gris = GRIS
  noir = NOIR
  bleu = BLEU
  vert = VERT
  jaune = JAUNE
  rouge = ROUGE
  */

  return couleur;
}

String VerificationSuiveurLigne()
{
  String etat;

  float voltage_recu;//Variable de voltage reçu du suiveur de ligne

  //À FAIRE(4) : Changement de voltage_recu en fonction du voltage reçu du suiveur de ligne
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

int VerificationZone()
{
  int zone;

  if(couleur != NOIR && couleur != GRIS && etat_suiveurLigne.charAt(1) == 'C') //Zone de couleur
  {
    if(zone == SUIVEUR_LIGNE)
    {
      //À FAIRE(6) : On avance le robot à coup de delay tant qu'une couleur autre que Blanc, Noir ou gris n'est pas détectée. (WHILE VerificationCouleur != ...)
    }

    zone = COULEUR;
  }
  else if(tour == 1 && etat_suiveurLigne.charAt(1) != 'C') //Zone du suiveur de ligne
  {
    zone = SUIVEUR_LIGNE;
  }
  else if(tour == 2 && etat_suiveurLigne.charAt(1) != 'C') //Zone du raccourci
  {
    //À FAIRE(7) : Avancer un peu, Tournant d'un angle sur lui-même vers la zone bleue pendant un delay

    zone = RACCOURCI;
  }
  else if(zone != SUIVEUR_LIGNE) //Zone de départ
  {
    if(couleur == NOIR) //Compte le nombre de fois de suite qui détecte du noir
    {
      suite_cycles_identiques++; 
    }
    else if(suite_cycles_identiques > 0) //La couleur noire n'est plus détectée de suite alors zone de départ //La valeur peut être ajustée plus grande que 0 pour éliminer des faux triggers de zone de départ en testant
    {
      zone = DEPART;
      suite_cycles_identiques = 0;
    }
  }

  return zone;
}

void cycle()
{
  couleur = VerificationCouleur(); //Vérification de la couleur
  etat_suiveurLigne = VerificationSuiveurLigne(); //Vérification de l'état du suiveur de ligne

  zone = VerificationZone(); //Vérification de la zone

  if(zone == COULEUR) //Zone de couleur
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
  else if(zone == SUIVEUR_LIGNE) //Zone du suiveur de ligne
  {
    //À FAIRE(5) : Suiveur de ligne, chaque fois que la gauche capte du noir, on tourne un peu à droite comme dans la zone de couleur. Chaque fois que la droite capte du noir, on tourne un peu à gauche
  }
  else if(zone == DEPART) //Ligne de départ/d'arrivée
  {
    if(tour++ == 2) //tour++ augmente le tour
      couleur_voulue = JAUNE; //Si c'est le 2e tour, couleur voulue = jaune.
    
    MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE));
    MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE));
  }
  else if(zone == RACCOURCI) //Zone du raccourci
  {
    //À FAIRE(8) : Détection des murs et avance tant que la couleur reste bleu, ensuite zone = COULEUR pour terminer le parcours.     
  }
  else //Zone grise ou zone du jump
  {
    MOTOR_SetSpeed(GAUCHE, VitesseCorrigee(V_VOULUE));
    MOTOR_SetSpeed(DROITE, VitesseCorrigee(V_VOULUE));
  }

  //À FAIRE(?) : Le fonctionnement du bras
}