#!/usr/bin/env python

import time
import RPi.GPIO as GPIO
from bmp280 import BMP280
from firebase import firebase

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(18, GPIO.OUT, initial=GPIO.LOW)

firebase = firebase.FirebaseApplication("https://greenops-smartbuilding-default-rtdb.firebaseio.com/", None)

try:
    from smbus2 import SMBus
except ImportError:
    from smbus import SMBus

print("""temperature-and-pressure.py - Displays the temperature and pressure.

Press Ctrl+C to exit!

""")

# Initialise the BMP280
bus = SMBus(1)
bmp280 = BMP280(i2c_dev=bus)

while True:
    GPIO.output(18, GPIO.LOW)
    temperature = bmp280.get_temperature()
    pressure = bmp280.get_pressure()
    print('{:05.2f}*C {:05.2f}hPa'.format(temperature, pressure))
    time.sleep(1)
    data = {
    'Temperature' : str(temperature) + '*C',
    'Pressure': str(pressure)  + ' hPa',
    }

    result = firebase.patch('Temperature and pressure', data)
    print(result)
    GPIO.output(18, GPIO.HIGH)
    time.sleep(2)

