#!/usr/bin/env python
# coding: utf-8

# In[]:

import RPi.GPIO as GPIO # Import Raspberry Pi GPIO library
from time import sleep # Import the sleep function from the time module

GPIO.setwarnings(False) # Ignore warning for now
GPIO.setmode(GPIO.BOARD) # Use physical pin numbering
GPIO.setup(18, GPIO.OUT, initial=GPIO.LOW) # Set pin 8 to be an output and set initial value to low (off)

while True: # Run forever
    GPIO.output(18, GPIO.HIGH) # Turn on
    sleep(1) # Sleep for one second
    GPIO.output(18, GPIO.LOW) # Turn off
    sleep(1) # Sleep for 1 second