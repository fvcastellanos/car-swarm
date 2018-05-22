//www.elegoo.com
//2016.09.19

// Servo library
#include<Servo.h>

volatile int state = LOW;
char command;

// Order of IN pins was changed because motor wiring were crossed
int in1=8;
int in2=9;
int in3=6;
int in4=7;
int ENA=11;
int ENB=5;
int ABS=150;

// Define parameter for ultrasonic sensor
Servo servo;
int Echo = A4;  
int Trig = A5; 

// Constants
int minimalDistance = 30;

// Control variables
boolean isMoving = false;
boolean goingForward = false;

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
  goingForward = true;
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
  goingForward = false;
  Serial.println("back");
}

void mLeft() {
  analogWrite(ENA, ABS);
  analogWrite(ENB, ABS);
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  isMoving = true;
  goingForward = false;
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
  goingForward = false;
  Serial.println("right");
}

void mStop() {
  digitalWrite(ENA,LOW);
  digitalWrite(ENB,LOW);

  isMoving = false;
  goingForward = false;
  Serial.println("stop");
}

void verifyFrontObstacles(int minDist, boolean moving, boolean forward) {

  if (moving && forward) {
    servo.write(90);
    delay(200);
    int currentDist = testDistance();
    
    if ((currentDist <= minDist)) {
      mStop();
  
      Serial.println("#");
    }    
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
  goingForward = false;
}

void loop() {

  // Calculating distances between nearest object when going forward
  verifyFrontObstacles(minimalDistance, isMoving, goingForward);
    
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
    
    case '1':
      mForward();
      delay(1000);
      mStop();
    break;

    case '2':
      mForward();
      delay(2000);
      mStop();
    break;

    case '3':
      mForward();
      delay(3000);
      mStop();
    break;

    case '4':
      mForward();
      delay(4000);
      mStop();
    break;

    case '5':
      mForward();
      delay(5000);
      mStop();
    break;
  
  }  
}

