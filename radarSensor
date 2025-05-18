#include <Servo.h>

const int trigPin = 8;
const int echoPin = 9;

Servo radarServo;

int angle = 0;
int direction = 1; // 1 = forward, -1 = reverse

void setup() {
  Serial.begin(9600);
  radarServo.attach(11);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  Serial.println("Angle,Distance(cm)");
}

void loop() {
  radarServo.write(angle);
  delay(30); // Let servo reach position

  long distance = readDistance();

  Serial.print(angle);
  Serial.print(",");
  Serial.println(distance);

  angle += direction;
  if (angle >= 180 || angle <= 0) {
    direction *= -1; // Reverse direction at ends
  }

  delay(50); // Delay for scan stability
}

long readDistance() {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  long duration = pulseIn(echoPin, HIGH, 30000); // Timeout after 30ms
  long distance = duration * 0.034 / 2;

  if (distance == 0 || distance > 400) {
    return 0; // No valid reading
  } else {
    return distance;
  }
}
