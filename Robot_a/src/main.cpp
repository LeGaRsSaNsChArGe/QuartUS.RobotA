#include <Arduino.h>
#include <stdio.h>
#include <LibRobus.h>
#include <math.h>

#define DEVANT -1
#define GAUCHE 0
#define DROITE 1

#define NEGATIF -1
#define POSITIF 1

#define V_MAX 1

//Déclaration des fonctions
float mm_a_pulses(float distance_mm);
float pulses_a_mm(float pulses);
float angleDegre_a_pulses(float angleDegre);
int32_t Acceleration(int direction, float d_pulses, float v_initiale, float v_finale, float v_actuelle);
void Deplacement(float angleDegre, float distance_mm);

void setup()
{
  // put your setup code here, to run once:
  BoardInit();
}

void loop()
{
  if(ROBUS_IsBumper(3))
  {
    //TESTS
    //Deplacement(0, 1000);
    Deplacement(90, 0);
    //Deplacement(-45, 0);

    //Deplacement(360, 0);
    //Deplacement(-360, 0);

    //PARCOURS
    Deplacement(0, 1225);
    Deplacement(-90, 0);
    Deplacement(0, 900);
    Deplacement(90, 0);
    Deplacement(0, 894);
    Deplacement(45, 0);
    Deplacement(0, 1850);
    Deplacement(-90, 0);
    Deplacement(0, );
    Deplacement(45, 0);
    Deplacement(0, );
  }
}

//Définitions des fonctions
float mm_a_pulses(float distance_mm)
{
  return (distance_mm/0.0748125);
}

float pulses_a_mm(float pulses)
{
  return (pulses*0.0748125);
}

float angleDegre_a_pulses(float angleDegre)
{
  return ((angleDegre * 96)/(360 * 38.1)) * 3200;
}

int32_t Acceleration(int direction, float d_pulses, float v_initiale, float v_finale, float* v_actuelle)
{
  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);

  if(v_initiale < v_finale)
    Serial.println("\t\t\tACCÉLÉRATION DIRECTE");
  else if(v_initiale > v_finale)
    Serial.println("\t\t\tACCÉLÉRATION OPPOSÉE");
  else if(v_initiale == v_finale)
    Serial.println("\t\tACCÉLÉRATION NULLE / VITESSE CONSTANTE");

  while((fabs(ENCODER_Read(GAUCHE)) < d_pulses) || ((d_pulses == -1) && (*v_actuelle != v_finale)))
  {
    //Accélération directe
    if((v_initiale < v_finale) && (*v_actuelle < v_finale))
      *v_actuelle += 0.05;
    //Accélération opposée
    else if((v_initiale > v_finale) && (*v_actuelle > v_finale))
      *v_actuelle -= 0.05;
    //Accélération nulle / Vitesse constante
    else if((v_initiale == v_finale) && (*v_actuelle == v_finale)) {}
    else
      return fabs(ENCODER_Read(GAUCHE));
    
    if(direction == DEVANT)
    {
      MOTOR_SetSpeed(GAUCHE, *v_actuelle);
      MOTOR_SetSpeed(DROITE, *v_actuelle);
    }
    else if(direction == GAUCHE)
    {
      MOTOR_SetSpeed(GAUCHE, -*v_actuelle);
      MOTOR_SetSpeed(DROITE, *v_actuelle);
    }
    else if(direction == DROITE)
    {
      MOTOR_SetSpeed(GAUCHE, *v_actuelle);
      MOTOR_SetSpeed(DROITE, -*v_actuelle);
    }
    else
      printf("valeur de direction impossible...");

    delay(1);

    //Serial.print("DISTANCE PARCOURUE: ");
    //Serial.print(pulses_a_mm(ENCODER_Read(GAUCHE)));
    Serial.print("\t\tVITESSE INITIALE: ");
    Serial.print(v_initiale);
    Serial.print("\t\tVITESSE ACTUELLE: ");
    Serial.print(*v_actuelle);
    Serial.print("\t\tVITESSE FINALE: ");
    Serial.println(v_finale);
  }
  return fabs(ENCODER_Read(GAUCHE));

  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);
}

//FONCTION TEST ------------------------------------------------------------------------------------------------------//
void Deplacement(float angleDegre, float distance_mm)// distance positive = avance || distance négative = recule
{
  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);

  float v_actuelle = 0;
  int direction = DEVANT;
  float d_pulses = mm_a_pulses(distance_mm);

  if(angleDegre < 0)
  {
    direction = GAUCHE;
    d_pulses = angleDegre_a_pulses(fabs(angleDegre));
  }
  else if(angleDegre > 0)
  {
    direction = DROITE;
    d_pulses = angleDegre_a_pulses(angleDegre);
  }

  Serial.print("\t\t\tDISTANCE TOTALE: ");
  Serial.println(pulses_a_mm(d_pulses));
  
  float d_pulses_parcourue = Acceleration(direction, d_pulses/2, v_actuelle, V_MAX, &v_actuelle);//Accélération

  Serial.print("DISTANCE PARCOURUE: ");
  Serial.print(pulses_a_mm(d_pulses_parcourue));
  Serial.print("\t\t\tVITESSE ACTUELLE: ");
  Serial.println(v_actuelle);

  if((d_pulses_parcourue < d_pulses/2) && (v_actuelle >= V_MAX))
  {
    Serial.print("\t\tDISTANCE PARCOURUE JUSQU'À VITESSE MAXIMUM: ");
    Serial.println(pulses_a_mm(d_pulses_parcourue));

    Serial.print("\t\tDISTANCE AVANT DÉCÉLÉRATION : ");
    Serial.println(pulses_a_mm(d_pulses - d_pulses_parcourue));

    d_pulses_parcourue += Acceleration(direction, d_pulses - (d_pulses_parcourue*2), /*v_actuelle*/v_actuelle, v_actuelle, &v_actuelle);//Vitesse constante
  }

  Serial.print("DISTANCE PARCOURUE: ");
  Serial.print(pulses_a_mm(d_pulses_parcourue));
  Serial.print("\t\t\tVITESSE ACTUELLE: ");
  Serial.println(v_actuelle);

  //d_pulses_parcourue += Acceleration(direction, d_pulses - d_pulses_parcourue, v_actuelle, 0, &v_actuelle);//Décélération
  d_pulses_parcourue += Acceleration(direction, -1, v_actuelle, 0, &v_actuelle);//Décélération

  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);

  if(angleDegre == 0)
  {
    Serial.print("DISTANCE PARCOURUE: ");
    Serial.print(pulses_a_mm(d_pulses_parcourue));
  }
  else
  {
    Serial.print("ANGLE EFFECTUÉE: ");
    Serial.print(((d_pulses_parcourue/3200)*(360*38.1))/96);
  }
  Serial.print("\t\tVITESSE FINALE: ");
  Serial.println(v_actuelle);

  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);
}

/*void DeplacementANCIEN(float distance_mm)
{
  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);

  float v_max = V_MAX;
  float v_actuelle = 0;

  Serial.print("\t\t\tDISTANCE TOTALE: ");
  Serial.println(distance_mm);

  Serial.println("\t\t\t\tACCÉLÉRATION");

  while((ENCODER_Read(GAUCHE) < (mm_a_pulses(distance_mm)/2)) && (v_actuelle < v_max))//Accélération
  {
    v_actuelle += 0.01;
    MOTOR_SetSpeed(GAUCHE, v_actuelle);
    MOTOR_SetSpeed(DROITE, v_actuelle);
    delay(1);

    Serial.print("DISTANCE PARCOURUE: ");
    Serial.print(pulses_a_mm(ENCODER_Read(GAUCHE)));
    Serial.print("\t\t\tVITESSE ACTUELLE: ");
    Serial.println(v_actuelle);
  }
  Serial.print("\t\t\tVITESSE ACTUELLE: ");
  Serial.println(v_actuelle);
  Serial.print("\t\t\tVITESSE MAXIMUM: ");
  Serial.println(v_max);

  if(v_actuelle >= v_max)//Vitesse constante
  {
    float pulses_v_max_atteinte = ENCODER_Read(GAUCHE);

    Serial.print("\t\tDISTANCE PARCOURUE JUSQU'À VITESSE MAXIMUM: ");
    Serial.println(pulses_a_mm(pulses_v_max_atteinte));

    Serial.println("\t\t\tVITESSE CONSTANTE");
    Serial.print("\t\tDISTANCE AVANT DÉCÉLÉRATION : ");
    Serial.println(pulses_a_mm(mm_a_pulses(distance_mm) - pulses_v_max_atteinte));

    while(ENCODER_Read(GAUCHE) < (mm_a_pulses(distance_mm) - pulses_v_max_atteinte))
    {
      delay(1);

      Serial.print("DISTANCE PARCOURUE: ");
      Serial.print(pulses_a_mm(ENCODER_Read(GAUCHE)));
      Serial.print("\t\t\tVITESSE ACTUELLE: ");
      Serial.println(v_actuelle);
    }
  }

  Serial.println("\t\t\t\tDÉCÉLÉRATION");
  while(v_actuelle > 0)//Décélération
  {
    v_actuelle -= 0.01;
    MOTOR_SetSpeed(GAUCHE, v_actuelle);
    MOTOR_SetSpeed(DROITE, v_actuelle);
    delay(1);

    Serial.print("DISTANCE PARCOURUE: ");
    Serial.print(pulses_a_mm(ENCODER_Read(GAUCHE)));
    Serial.print("\t\t\tVITESSE ACTUELLE: ");
    Serial.println(v_actuelle);
  }

  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);
}*/

/*void Rotation(int angleDegre, int direction)
{
  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);

  float nbpulse = angleDegre * (9.65/(3.81 * 360)) * 3200;
  Serial.print("Nombre de pulses voulu:");
  Serial.println(nbpulse);
  delay(1000);

  while (sqrt(pow(ENCODER_Read(DROITE) - ENCODER_Read(GAUCHE), 2)) < 2 * nbpulse)
  {
    Serial.print("GAUCHE: ");
    Serial.print(ENCODER_Read(GAUCHE));
    Serial.print("\t\t\tDROITE: ");
    Serial.println(ENCODER_Read(DROITE));

    if(direction == GAUCHE)
    {
      MOTOR_SetSpeed(GAUCHE, -V_MAX);
      MOTOR_SetSpeed(DROITE, V_MAX);
    }
    else if(direction == DROITE)
    {
      MOTOR_SetSpeed(GAUCHE, V_MAX);
      MOTOR_SetSpeed(DROITE, -V_MAX);
    }
    else
      printf("valeur de direction erronee");
  }

  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);
  ENCODER_Reset(GAUCHE);
  ENCODER_Reset(DROITE);
}*/
