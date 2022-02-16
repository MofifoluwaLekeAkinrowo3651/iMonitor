import pyrebase
# import firebase_admin
# from firebase_admin import db
# from firebase_admin import credentials
# 
# cred = credentials.Certificate("path/to/serviceAccountKey.json")
# firebase_admin.initialize_app(cred)

config = {
            "apiKey": "AIzaSyAf4jUiSSAVtKqj4wfnDgJr_nmvoTZmmvg",
            "authDomain": "greenops-smartbuilding.firebaseapp.com",
            "databaseURL": "https://greenops-smartbuilding-default-rtdb.firebaseio.com/",
            "storageBucket": "greenops-smartbuilding.appspot.com"          
         }
class firebase:
    def __init__(self) -> None:
        self.firebase = pyrebase.initialize_app(config)
        self.db = self.firebase.database()

    def set_distance(self, child, value):
        self.db.child(child).set(value)
        print('Distance Updated!')
