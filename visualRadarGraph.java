import processing.serial.*;

Serial arduinoPort;

String data = "";
float angle = 0;
float distance = 0;

// Store past positions for motion trails (optional)
ArrayList<PVector> trail = new ArrayList<PVector>();

void setup() {
  size(600, 600);
  background(0);
  smooth();

  // LIST ALL SERIAL PORTS TO FIND ARDUINO
  println("Available serial ports:");
  println(Serial.list());

  // Manually select the Arduino port for macOS
  // Replace with your exact port name (e.g., "/dev/tty.usbmodem14101")
  String portName = findArduinoPort(); // Use helper function below
  arduinoPort = new Serial(this, portName, 9600);
  arduinoPort.bufferUntil('\n');
}

void draw() {
  background(0);
  translate(width / 2, height - 50);
  drawRadarGrid();

  // Convert polar to cartesian
  if (distance > 0 && distance < 400) {
    float r = map(distance, 0, 400, 0, 250);
    float x = r * cos(radians(angle));
    float y = -r * sin(radians(angle));
    
    PVector pos = new PVector(x, y);
    trail.add(pos);

    fill(0, 255, 0);
    noStroke();
    ellipse(x, y, 10, 10);
  }

  // Draw trail
  for (int i = max(0, trail.size() - 20); i < trail.size(); i++) {
    PVector t = trail.get(i);
    fill(0, 255, 0, map(i, 0, trail.size(), 20, 255));
    ellipse(t.x, t.y, 5, 5);
  }

  // Limit trail length
  if (trail.size() > 50) {
    trail.remove(0);
  }
}

void serialEvent(Serial arduinoPort) {
  data = trim(arduinoPort.readStringUntil('\n'));
  if (data != null && data.contains(",")) {
    String[] parts = split(data, ',');
    if (parts.length == 2) {
      angle = float(parts[0]);
      distance = float(parts[1]);
    }
  }
}

void drawRadarGrid() {
  stroke(0, 255, 0);
  noFill();
  for (int i = 100; i <= 400; i += 100) {
    arc(0, 0, i, i, PI, TWO_PI);
  }

  for (int a = 0; a <= 180; a += 30) {
    float x = 250 * cos(radians(a));
    float y = -250 * sin(radians(a));
    line(0, 0, x, y);
  }

  stroke(0, 200, 0);
  line(0, 0, 250 * cos(radians(angle)), -250 * sin(radians(angle))); // sweep arm
}

// Utility function to help find Arduino serial port on macOS
String findArduinoPort() {
  String[] ports = Serial.list();
  for (String port : ports) {
    if (port.toLowerCase().contains("usbmodem") || port.toLowerCase().contains("usbserial")) {
      return port;
    }
  }
  return ports[0]; // fallback if not found
}
