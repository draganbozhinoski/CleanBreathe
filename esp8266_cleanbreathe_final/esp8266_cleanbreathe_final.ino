#include <SdsDustSensor.h>
#include <Wire.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#define MINUTE 60000

const int analogNoisePin = A0;
const int rxdPin = D5;
const int txdPin = D6;

int8_t SCLPin = D1;
int8_t SDAPin = D2;
// = = = = PINS = = = = ^
const char* ssid = "ssid";
const char* password = "password";
String host = "host";

SoftwareSerial softwareSerial(rxdPin, txdPin);
SdsDustSensor sds(softwareSerial); //  additional parameters: retryDelayMs and maxRetriesNotAvailable

void setup() {
  Serial.begin(9600); 
  sds.begin();
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
     delay(1500);
     Serial.print(".");
  }
  Serial.print("WiFi connected - ");
  Serial.println(WiFi.localIP());
}

void loop() {
  
  //PM 10 PM 25 SENSOR =============================================================
  sds.wakeup();
  delay(10000); 
  PmResult pm = sds.queryPm();
  if(pm.isOk()) {
    Serial.print("PM2.5 = ");
    Serial.println(pm.pm25);
    Serial.print("PM10 = ");
    Serial.println(pm.pm10);
  }
  else { 
    Serial.print("Could not read values from the sensor!");
    Serial.println(pm.statusToString());
  }
  WorkingStateResult state = sds.sleep();
  //PM 10 PM 25 SENSOR DONE =============================================================
  //NOISE SENSOR =============================================================
  long sum = 0;
  for(int i=0; i<32; i++)
  {
      sum += analogRead(analogNoisePin);
  }
  sum >>= 5;
  int db = map(sum,20,900,49.5,90);
  Serial.print("Noise: ");
  Serial.println(db);
  //NOISE SENSOR DONE ===============================================
  if(WiFi.status()== WL_CONNECTED){
    WiFiClient client;
    HTTPClient http;
    String request = host + "/api/save";
    Serial.println("Requesting url: "+ request);
    if(http.begin(client,request)) {
      http.addHeader("Content-Type", "application/x-www-form-urlencoded");
      String requestData = "pm10="+String(pm.pm10)+"&pm25="+String(pm.pm25)+"&noise="+String(db);
      int httpResponseCode = http.POST(requestData);
      delay(5000);
      if (httpResponseCode>0) {
        Serial.print("Response from server: ");
        Serial.println(httpResponseCode);
        String payload = http.getString();
        Serial.println(payload);
      }
      else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      http.end();
    }
    else {
      Serial.println("WiFi Disconnected");
      WiFi.begin(ssid,password);
    }
  }
  if(WiFi.status() ==WL_CONNECTED){
    Serial.println("Delaying 3 minutes..");
    delay(3*MINUTE);
  }
  else {
    WiFi.begin(ssid,password);
  }
}
