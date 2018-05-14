//www.elegoo.com
//2016.09.19

// Servo library
#include<Servo.h>

volatile int state = LOW;
char command;

// Order of IN pins were changed because motor wiring were crossed
int in1=8;
int in2=9;
int in3=6;
int in4=7;
int ENA=10;
int ENB=5;
int ABS=135;

// Define parameter for ultrasonic sensor
Servo servo;
int Echo = A4;  
int Trig = A5; 

// Constants
int minimalDistance = 50;

// Control variables
boolean isMoving;
char currentDirection;

int testDistance()   
{
  digitalWrite(Trig, LOW);   
  delayMicroseconds(2);
  digitalWrite(Trig, HIGH);  
  delayMicroseconds(20);
  digitalWrite(Trig, LOW);   
  float fDistance = pulseIn(Echo, HIGH);  
  fDistance= fDistance/58;       
  return (int)fDistance;
}  

void mForward() {   
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);
  digitalWrite(in1,LOW);
  digitalWrite(in2,HIGH);
  digitalWrite(in3,LOW);
  digitalWrite(in4,HIGH);

  isMoving = true;
  currentDirection = 'f';
  Serial.println("forward");
}

void mBack() {
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);
  digitalWrite(in1,HIGH);
  digitalWrite(in2,LOW);
  digitalWrite(in3,HIGH);
  digitalWrite(in4,LOW);

  isMoving = true;
  currentDirection = 'b';
  Serial.println("back");
}

void mLeft() {
  analogWrite(ENA,ABS);
  analogWrite(ENB,ABS);
  digitalWrite(in1,LOW);
  digitalWrite(in2,HIGH);
  digitalWrite(in3,HIGH);
  digitalWrite(in4,LOW);

  isMoving = true;
  currentDirection = 'l';  
  Serial.println("left");
}

void mRight() {
  analogWrite(ENA,ABS);
  analogWrite(ENB,ABS);
  digitalWrite(in1,HIGH);
  digitalWrite(in2,LOW);
  digitalWrite(in3,LOW);
  digitalWrite(in4,HIGH);

  isMoving = true;
  currentDirection = 'r';  
  Serial.println("right");
}

void mStop() {
  digitalWrite(ENA,LOW);
  digitalWrite(ENB,LOW);

  isMoving = false;
  currentDirection = 's';  
  Serial.println("stop");
}

void verifyPanicAttack(int minDist, boolean moving, char dir) {
  servo.write(90);
  delay(500);
  int currentDist = testDistance();
  
  if ((currentDist <= minDist) && moving && (dir == 'f')) {
    mStop();

    Serial.println("panic");
  }
}

void pong() {
  Serial.println("ping...pong");
}

void setup() {

  // Setup serial comm
  Serial.begin(9600);

  // Setup ultrasonic
  servo.attach(3);
  pinMode(Echo, INPUT);    
  pinMode(Trig, OUTPUT);  
  
  // Setup engines
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(ENA, OUTPUT);
  pinMode(ENB, OUTPUT);

  // Stop vehicle
  digitalWrite(ENA, LOW);
  digitalWrite(ENB, LOW);  
  isMoving = false;
  currentDirection = 's';
}

void loop() {

  // Calculating distances between nearest object when going forward
  verifyPanicAttack(minimalDistance, isMoving, currentDirection);
    
  // Reading incomming command
  command = Serial.read();

  switch(command) {
    case 'f':
      mForward();
    break;
    case 'b':
      mBack();
    break;
    case 'l':
      mLeft();
    break;
    case 'r':
      mRight();
    break;
    case 's':
      mStop();
    break;
    case 'p':
      pong();
    break;
  }  
}

