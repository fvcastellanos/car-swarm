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
int minimalDistance = 50;
int TURN_MS_DELAY = 555;

float meanVelocity = 0.9337; // mts/s

// Control variables
boolean isMoving = false;
boolean goingForward = false;

unsigned long timer;

int calculateDelay(float velocity, float distance) {
  if (distance > 0) {
    return (int)(velocity / distance);
  }

  return 0;
}

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
}

void mStop() {
  digitalWrite(ENA,LOW);
  digitalWrite(ENB,LOW);

  isMoving = false;
  goingForward = false;
}

int convertDistanceToTimeMillis(float distance) {
  //  t = d / v
  float seconds = distance / meanVelocity; // time in secons
  return seconds * 1000; // convert seconds to milliseconds  
}

boolean nonBlockingDelay(int timeInMillis) {

  if (millis() - timer >= timeInMillis) {
    return true;
  }  

  return false;
}

void turnDirection(char dir) {

  if (!isMoving) {

    timer = millis();

    switch(dir) {
      case 'l':
        mLeft();
      break;
      case 'r':
        mRight();
      break;
    }    
  }

  if (nonBlockingDelay(TURN_MS_DELAY)) {
    mStop();
    Serial.println('t');
  }
}

void moveStraight(char dir, float distance) {

  if (!isMoving) {

    timer = millis();
    switch(dir) {
      case 'f':
        mForward();
      break;
      case 'b':
        mBack();
      break;
    }    
  }

  int waitMillis = convertDistanceToTimeMillis(distance);

  if (nonBlockingDelay(waitMillis)) {
    mStop();
    Serial.println('t');
  }
  
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

  // Setting timer to zero
  timer = 0;
}

void loop() {

  if (isMoving) {
    // Calculating distances between nearest object when going forward
    verifyFrontObstacles(minimalDistance, isMoving, goingForward);  
  }
    
  // Reading incomming command
  if (!isMoving) {
    command = Serial.read();    
  }

  switch(command) {
    case 'f':
      moveStraight('f', 1); // move 1 mt. forward
    break;
    case 'b':
      moveStraight('b', 1); // move 1 mt. backwards
    break;
    case 'l':
      turnDirection('l');
    break;
    case 'r':
      turnDirection('r');
    break;
    case 's':
      mStop();
    break;
    case 'p':
      pong();
    break;    
  }  
}

