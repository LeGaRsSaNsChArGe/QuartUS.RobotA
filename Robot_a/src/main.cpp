#include <Arduino.h>
#include <stdio.h>
#include <librobus.h>

#define GAUCHE 0
#define DROITE 1

//Déclaration des fonctions
void Rotation(int angleDegre, int direction);

void setup()
{
  // put your setup code here, to run once:
  BoardInit();
}

void loop()
{
  // put your main code here, to run repeatedly:
  Rotation(90, GAUCHE);
  Rotation(45, DROITE);
  Rotation(405, GAUCHE);

}

//Définitions des fonctions

void Rotation(int angleDegre, int direction)
{
  ENCODER_ReadReset(GAUCHE);
  ENCODER_ReadReset(DROITE);

  float nbpulse = angleDegre * (9.65/3.81) * 3200;

  if(direction == GAUCHE)
  {
   while (ENCODER_Read(GAUCHE) + ENCODER_Read(DROITE) < 2 * nbpulse)
   {
    MOTOR_SetSpeed(GAUCHE, -0.5);
    MOTOR_SetSpeed(DROITE, 0.5);
   }
  }
  else if(direction == DROITE)
  {
   while (ENCODER_Read(GAUCHE) + ENCODER_Read(DROITE) < 2 * nbpulse)
   {
    MOTOR_SetSpeed(GAUCHE, 0.5);
    MOTOR_SetSpeed(DROITE, -0.5);
   }
  }
  else
  {
    printf("valeur de direction erronee");
  }
  ENCODER_ReadReset(GAUCHE);
  ENCODER_ReadReset(DROITE);
}
