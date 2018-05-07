
import serial
import time

comPort = "COM4"

ser = serial.Serial(
    port=comPort,
    baudrate=9600,
    timeout=0.3,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS
)

print("port used: ", ser.name)

while 1:
    text = input("enter a command: ")
    if text == "quit":
        break

    ser.write(text.encode('utf-8'))
    time.sleep(0.3)

    response = ser.readline()
    # response = ser.read(12)

    print("response: ", response.decode('utf-8'))

ser.close()