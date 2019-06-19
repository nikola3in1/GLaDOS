
// Message key
String key;
// Message value
String value;

// Motors
int motor1PIN=8;
int motor2PIN=9;
int motor3PIN=10;
int motor4PIN=11;

// Motor values
int motor1Val = 90;
int motor2Val = 90;
int motor3Val = 90;
int motor4Val = 90;

// Motors min/max values
int motor1Max = 180;
int motor1Min = 0;
int motor2Max = 180;
int motor2Min = 0;
int motor3Max = 180;
int motor3Min = 0;
int motor4Max = 180;
int motor4Min = 0;

void setup() {
  Serial.begin(9600);
}

void loop() {
  // Cekamo input
  while(!Serial.available()){}
  key = Serial.readString();
  key.trim();

  blinkLed();
  
  if(key.length() > 0){
   boolean isNegative = false;    // promenljiva u kojoj cuvamo da li je vrednost negativna
   int valueInt = 0;   // promenljiva u kojoj cuvamo parsiranu vrednost
   
   //Proveravamo da li je vrednost negativna
   if(key.indexOf('-') > 0){
      isNegative=true;
      value = key.substring(key.indexOf('-')+1);
   }else{
      // Vrednost je pozitivna
      value = key.substring(key.indexOf(':')+1);
   }
   // Parsirama kljuc
   key = key.substring(0,key.indexOf(':'));

  delay(500);

   // Provera da li je vrednost int, ako nije izadji
   if(!isDigit(value[0])){
    return;
   }

   // Parsiramo String vrednost u int
   valueInt = value.toInt();
   // Provera da li je vrednost negativna
   // ako jeste, mnozimo je sa -1;
   if(isNegative){
     valueInt*=-1;
   }

   //Validacija i azuriranje pozicije motora
   if(key == "motor1" && validateValue(motor1PIN,valueInt)){
     moveMotor(motor1PIN, motor1Val, valueInt);
     // Sending the updated value of the motor
     sendResponse(motor1PIN, motor1Val);
   
   }else if(key == "motor2" && validateValue(motor2PIN,valueInt)){
     moveMotor(motor2PIN, motor2Val, valueInt);
     // Sending the updated value of the motor
     sendResponse(motor2PIN, motor2Val);
   
   }else if(key== "motor3" && validateValue(motor3PIN,valueInt)){
     moveMotor(motor3PIN, motor3Val, valueInt);
     // Sending the updated value of the motor
     sendResponse(motor3PIN, motor3Val);
   
   }else if(key == "motor4"&& validateValue(motor4PIN,valueInt)){     
     moveMotor(motor4PIN, motor4Val, valueInt); 
     // Sending the updated value of the motor
     sendResponse(motor4PIN, motor4Val);   
   }
 } 
}

void blinkLed(){
  digitalWrite(13, HIGH);
  delay(300);
  digitalWrite(13, LOW);
  delay(300);  
  digitalWrite(13, HIGH);
}

void reset(){
  Serial.println("reset");
  key="";
  value="";  
}

boolean validateValue(int motorPin, int value){
  if(motorPin == motor1PIN){
    int newVal = motor1Val + value;
    if(newVal < motor1Max && newVal > motor1Min){
      return true;
    }  
  } else if(motorPin == motor2PIN){
    int newVal = motor2Val + value;
    if(newVal < motor2Max && newVal > motor2Min){
      return true;
    }  
  } else if(motorPin == motor3PIN){
    int newVal = motor3Val + value;
    if(newVal < motor3Max && newVal > motor3Min){
      return true;
    }  
  } else if(motorPin == motor4PIN){
    int newVal = motor4Val + value;
    if(newVal < motor4Max && newVal > motor4Min){
      return true;
    }  
  }
  //Vrati negativan response code
  sendResponse(motorPin,-1);
  return false;
  
}

// Updating motor position,
// args: int motorPin - pin of the motor
//       int motorVal - old value of the motor
//       int value    - value to be added to the
//                    old value of the motor
void moveMotor(int motorPin, int& motorVal ,int value){
  int newValue = motorVal + value;
  motorVal = newValue;
  // update pin
}

void sendResponse(int motorPin, int value){
  String response = "motor:";
  response.concat(motorPin);
  response.concat(":");
  response.concat(value);
  Serial.println(response);
}
