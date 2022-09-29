#include <Arduino.h>
#include <stdio.h>
#include <LibRobus.h>

#define GAUCHE 0
#define DROITE 1

#define NEGATIVE -1
#define POSITIVE 1

//Déclaration des fonctions
float mm_a_pulse(float distance_mm);
void AccelerationRoue(int ID_roue, float distance_mm, float v_initiale, float v_finale);
void deplacementRoue(int ID_roue, float distance_mm, float v_initiale);
void Rotation(int angleDegre, int direction);

void setup()
{
  // put your setup code here, to run once:
  BoardInit();
}

void loop()
{
  // put your main code here, to run repeatedly:
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);
  Rotation(45, GAUCHE);

  //deplacementRoue(GAUCHE, 400, 0);
  //deplacementRoue(DROITE, 400, 0);

}

//Définitions des fonctions
float mm_a_pulse(float distance_mm)//TEMPORAIRE
{
  return (distance_mm*13.37);
}

void AccelerationRoue(int ID_roue, float distance_mm, float v_initiale, float v_finale)
{
  float v = v_initiale;

  if(v_initiale < v_finale)
  {
    while(v <= v_finale)
    {
      delay((distance_mm/0.1)/1000);
      v = v + v_finale/1000;
      MOTOR_SetSpeed(ID_roue, v);
    }
  }
  else if(v_initiale > v_finale)
  {
    while(v >= v_finale)
    {
      delay((distance_mm/0.1)/1000);
      v = v - v_initiale/1000;
      MOTOR_SetSpeed(ID_roue, v);
    }
  }
  else
  {
    //
  }
}

void deplacementRoue(int ID_roue, float distance_mm, float v_initiale)
{
  float v_max = 0.5;
  //v_initiale sera codé plus tard et devra changer la valeur de 40 minimum
  if(distance_mm >= 400) //Pour v_initiale = 0
  {    
    AccelerationRoue(ID_roue, 200, v_initiale, v_max);
    ENCODER_ReadReset(ID_roue);

    while(ENCODER_Read(ID_roue) < mm_a_pulse(distance_mm - 400))
      MOTOR_SetSpeed(ID_roue, v_max);
    ENCODER_ReadReset(ID_roue);

    AccelerationRoue(ID_roue, 200, v_max, v_initiale);
    ENCODER_ReadReset(ID_roue);
  }
  else if(distance_mm < 400)
  {
    //À concevoir
  }
  else
  {
    //erreur, exemple un nomre négatif
  }
}

void Rotation(int angleDegre, int direction)
{
  ENCODER_ReadReset(GAUCHE);
  ENCODER_ReadReset(DROITE);

  float v_max = 0.2;
  float nbpulse = angleDegre * (9.65/(3.81 * 360)) * 3200;
  Serial.print("Nombre de pulses voulu:");
  Serial.println(nbpulse);
  delay(1000);

  if(direction == GAUCHE)
  {
   while (ENCODER_Read(DROITE) - ENCODER_Read(GAUCHE) < 2 * nbpulse)
   {
    Serial.print("GAUCHE: ");
    Serial.print(ENCODER_Read(GAUCHE));
    Serial.print("\t\t\tDROITE: ");
    Serial.println(ENCODER_Read(DROITE));

    MOTOR_SetSpeed(GAUCHE, -v_max);
    MOTOR_SetSpeed(DROITE, v_max);
   }
  }
  else if(direction == DROITE)
  {
   while (ENCODER_Read(GAUCHE) - ENCODER_Read(DROITE) < 2 * nbpulse)
   {
    Serial.print("GAUCHE: ");
    Serial.print(ENCODER_Read(GAUCHE));
    Serial.print("\t\t\tDROITE: ");
    Serial.println(ENCODER_Read(DROITE));

    MOTOR_SetSpeed(GAUCHE, v_max);
    MOTOR_SetSpeed(DROITE, -v_max);
   }
  }
  else
  {
    printf("valeur de direction erronee");
  }

  MOTOR_SetSpeed(GAUCHE, 0);
  MOTOR_SetSpeed(DROITE, 0);
  ENCODER_ReadReset(GAUCHE);
  ENCODER_ReadReset(DROITE);
}
