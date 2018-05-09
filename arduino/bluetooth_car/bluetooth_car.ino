//www.elegoo.com
//2016.09.19

int LED=13;
volatile int state = LOW;
char getstr;
//int in1=9;
//int in2=8;
//int in3=7;
//int in4=6;
// Order of in pins changed because motors where crossed
int in1=8;
int in2=9;
int in3=6;
int in4=7;
int ENA=10;
int ENB=5;
int ABS=135;
void _mForward()
{ 
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);
  digitalWrite(in1,LOW);
  digitalWrite(in2,HIGH);
  digitalWrite(in3,LOW);
  digitalWrite(in4,HIGH);
  
  Serial.println("go forward!");
}
void _mBack()
{
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);
  digitalWrite(in1,HIGH);
  digitalWrite(in2,LOW);
  digitalWrite(in3,HIGH);
  digitalWrite(in4,LOW);
  Serial.println("go back!");
}
void _mleft()
{
  analogWrite(ENA,ABS);
  analogWrite(ENB,ABS);
  digitalWrite(in1,LOW);
  digitalWrite(in2,HIGH);
  digitalWrite(in3,HIGH);
  digitalWrite(in4,LOW);
  Serial.println("go left!");
}
void _mright()
{
  analogWrite(ENA,ABS);
  analogWrite(ENB,ABS);
  digitalWrite(in1,HIGH);
  digitalWrite(in2,LOW);
  digitalWrite(in3,LOW);
  digitalWrite(in4,HIGH);
  Serial.println("go right!");
}
void _mStop()
{
  digitalWrite(ENA,LOW);
  digitalWrite(ENB,LOW);
  Serial.println("Stop!");
}
void stateChange()
{
  state = !state;
  digitalWrite(LED, state);
}
void _pong()
{
  Serial.println("ping...pong");
}
void setup()
{
  pinMode(LED, OUTPUT);
  Serial.begin(9600);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(ENA, OUTPUT);
  pinMode(ENB, OUTPUT);

  // Stop ?
  digitalWrite(ENA, LOW);
  digitalWrite(ENB, LOW);  
//  _mStop();
}
void loop()
{
  getstr=Serial.read();
  if(getstr=='f')
  {
    _mForward();
  }
  else if(getstr=='b')
  {
    _mBack();
    delay(200);
  }
  else if(getstr=='l')
  {
    _mleft();
    delay(200);
  }
  else if(getstr=='r')
  {
    _mright();
    delay(200);
  }
  else if(getstr=='s')
  {
     _mStop();     
  }
  else if (getstr == 'A')
  {
//    stateChange();
  }
  else if (getstr == 'p')
  {
    _pong();
  }
}

