#Libraries
import RPi.GPIO as GPIO
import time
import firebase_link

db = firebase_link.firebase()
 
GPIO.setwarnings(False) # Ignore warnings

#GPIO Mode (BOARD / BCM)
GPIO.setmode(GPIO.BCM)
 
#set GPIO Pins
TRIG1 = 27
ECHO1 = 17
LED = 24
 
print ("Distance Measurement In Process")
GPIO.setup(TRIG1, GPIO.OUT)
GPIO.setup(ECHO1, GPIO.IN)
GPIO.setup(LED, GPIO.OUT, initial=GPIO.LOW) # Set pin 8 to be an output and set initial value
GPIO.output(TRIG1, GPIO.LOW)

print ("Waiting For HC-SR04 Sensor To Settle")
time.sleep(2)
print ("Calculating Distance")
GPIO.output(TRIG1, GPIO.HIGH)
time.sleep(.00001)
GPIO.output(TRIG1, GPIO.LOW)

while GPIO.input(ECHO1) == 0:
    pulse_start1 = time.time()
 
while GPIO.input(ECHO1) == 1:
    pulse_end1 = time.time()

pulse_duration1 = pulse_end1 - pulse_start1

distance1 = pulse_duration1 * 17150
distance1 = round(distance1, 2)
db.set_distance("distance", distance1)

if distance1 > 0.00 and distance1 < 15:
        print("Distance: ",distance1, "cm")
        GPIO.output(LED, GPIO.HIGH) # Turn on
        time.sleep(2) 
        GPIO.output(LED, GPIO.LOW) # Turn off
        time.sleep(1)
        GPIO.output(LED, GPIO.HIGH) # Turn on
        time.sleep(2)
        GPIO.output(LED, GPIO.LOW) # Turn off
        time.sleep(1)
        GPIO.output(LED, GPIO.HIGH) # Turn on
        time.sleep(3)
        
else:
        #print "Distance: ",distance1, "cm"
        GPIO.output(LED, GPIO.HIGH) # Turn on
        time.sleep(25)

GPIO.cleanup()