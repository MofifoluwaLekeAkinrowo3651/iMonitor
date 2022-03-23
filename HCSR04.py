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

def dist():

    GPIO.output(TRIG1, True)
    # set Trigger after 0.01ms to LOW
    time.sleep(0.00001)
    GPIO.output(TRIG1, False)

    while GPIO.input(ECHO1) == 0:
        pulse_start1 = time.time()
 
    while GPIO.input(ECHO1) == 1:
        pulse_end1 = time.time()

    pulse_duration1 = pulse_end1 - pulse_start1

    distance1 = pulse_duration1 * 17150
    distance1 = round(distance1, 2)
    return distance1

print ("Waiting For HC-SR04 Sensor To Settle")
time.sleep(2)
print ("Calculating Distance")
GPIO.output(TRIG1, GPIO.HIGH)
time.sleep(1)
GPIO.output(TRIG1, GPIO.LOW)
            
while True:
    result = dist()
    print("Distance: ", result, "cm")
    time.sleep(1)
    db.set_distance("distance", result)


GPIO.cleanup()
