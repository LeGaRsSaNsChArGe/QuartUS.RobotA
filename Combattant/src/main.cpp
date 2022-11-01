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

#define DELrouge 24
#define DELbleu 32
#define DELvert 28
#define DELjaune 26

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

  //Initialisation des DEL à LOW
  pinMode(DELrouge, OUTPUT);
  pinMode(DELbleu, OUTPUT);
  pinMode(DELvert, OUTPUT);
  pinMode(DELjaune, OUTPUT);
  digitalWrite(DELrouge, LOW);
  digitalWrite(DELbleu, LOW);
  digitalWrite(DELvert, LOW);
  digitalWrite(DELjaune, LOW);
  
  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);

  signal_sifflet = false;
  tour = 0; //Avant de traverser la ligne de départ

  couleur_voulue = VerificationCouleur(); //Vérification de la couleur au sol au moment du setup, soit vert ou jaune.
}

//Main--------------------------------------------------------
void loop()
{
  int temps_5hz = 0;

  while(analogRead(A10) < analogRead(A11))
  {
    delay(50);
    temps_5hz += 50;

    Serial.print(analogRead(A10));
    Serial.print("\t");
    Serial.println(analogRead(A11));

    Serial.println(temps_5hz);
  }

  if (temps_5hz > 1900 && temps_5hz < 2100)
  {
    Serial.println("SIFFLET = TRUE");
    
    signal_sifflet = true; //Si le signal correspond à 5 kHz pendant 2 secondes
    switch(couleur_voulue) //Allume la DEL correspondante à la variable couleur_voulue
    {
      case ROUGE:
      digitalWrite(DELrouge, HIGH);
      break;
      case BLEU:
      digitalWrite(DELbleu, HIGH);
      break;
      case VERT:
      digitalWrite(DELvert, HIGH);
      break;
      case JAUNE:
      digitalWrite(DELjaune, HIGH);
      break;
      default:
      break;
    }
  }

  if(signal_sifflet && tour <= 2 && (millis() - t_actuel_cycle > TEMPS_CYCLE)) //Mettez en commentaire et faite votre propre code dans le loop si vous testez des sections ou des fonctions.
  {
    t_actuel_cycle = millis();
    //cycle(); //Le cycle du robot à faire à chaque intervalle de TEMPS_CYCLE


    //TESTS
    etat_suiveurLigne = VerificationSuiveurLigne(); //Vérification de l'état du suiveur de ligne
    if(etat_suiveurLigne.charAt(1) == 'G') //Ne détecte plus de BLANC à GAUCHE
    {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(0.8));
    }
    else if(etat_suiveurLigne.charAt(1) == 'D') //Ne détecte plus de BLANC à DROITE
    {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE*0.8);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(1.25));
    }
    else
    {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(1));
    }
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

  return JAUNE;
}

String VerificationSuiveurLigne()
{
  String etat;
  float voltage_recu = analogRead(A12)*5/1023;//Variable de voltage reçu du suiveur de ligne

  /*
  G : détection de noir à gauche du robot
  C : détection de noir au centre du robot
  D : détection de noir à droite du robot
  */

  if( 5 > voltage_recu > 4.9)
    etat = "---";
  else if(3.7 > voltage_recu > 3.5)
    etat = "--D";
  else if(2.2 > voltage_recu > 2.05)
    etat = "-C-";
  else if(4.35 > voltage_recu > 4.2)
    etat = "G--";
  else if(0.8 > voltage_recu > 0.6)
    etat = "-CD";
  else if(2.9 > voltage_recu > 2.8)
    etat = "G-D";
  else if(1.45 > voltage_recu > 1.35)
    etat = "GC-";
  else 
    etat = "GCD";

  return etat;
}

int VerificationZone()
{
  int zone;

  if(couleur != NOIR && couleur != GRIS && couleur != BLANC) //Zone de couleur
  {
    /*if(zone == SUIVEUR_LIGNE)
    {
      while(VerificationCouleur() == NOIR || VerificationCouleur() == BLANC) //Avance le robot jusqu'à ce que le capteur ne détecte plus de NOIR ou de BLANC
      {
        MOTOR_SetSpeed(GAUCHE, V_VOULUE);
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(1));
      }
    }*/

    zone = COULEUR;
  }
  else if(tour == 1 && couleur == BLANC) //Zone du suiveur de ligne
  {
    zone = SUIVEUR_LIGNE;
  }
  else if(tour == 2 && couleur == BLANC) //Zone du raccourci
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
        MOTOR_SetSpeed(GAUCHE, V_VOULUE);
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(1));
        break;
      case 1 :
      case 2 :
        MOTOR_SetSpeed(GAUCHE, V_VOULUE);
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(0.8));
        break;
      case -1 :
      case -2 :
        MOTOR_SetSpeed(GAUCHE, V_VOULUE*0.8);
        MOTOR_SetSpeed(DROITE, VitesseCorrigee(1.25));
        break;
      default:
        break;
    }
  }
  else if(zone == SUIVEUR_LIGNE) //Zone du suiveur de ligne
  {
   if(etat_suiveurLigne.charAt(1) == 'G') //Ne détecte plus de BLANC à GAUCHE
   {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(0.8));
   }
   else if(etat_suiveurLigne.charAt(1) == 'D') //Ne détecte plus de BLANC à DROITE
   {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE*0.8);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(1.25));
   }
   else
   {
      MOTOR_SetSpeed(GAUCHE, V_VOULUE);
      MOTOR_SetSpeed(DROITE, VitesseCorrigee(1));
   }
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