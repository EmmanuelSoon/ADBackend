from tkinter import Image
import numpy as np 
from flask import Flask, request, jsonify
from functions import *
import base64
import io
import PIL.Image as Image
import tensorflow as tf

train_dir = 'E:/AD project/Web scraping/ML/images/'


app = Flask(__name__)

# Load the model
model = loadModel('model.json', 'model.h5')
model.compile(
    optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"]
)
train = create_df(train_dir)


@app.route('/predict_api',methods=['POST'])
def predict():
    print('test')
    # Get the data from the POST request.
    data = request.get_data()
    # data = request.get_json(force=True) 

    # convert json string back into img   
    img = Image.open(io.BytesIO(data))
    from numpy import asarray
    resized = tf.image.resize(img, (224,224))

    numpydata = asarray(resized, dtype=int) 
    numpydata = np.expand_dims(numpydata, 0)
    numpydata = np.expand_dims(numpydata, -1)

    # Make prediction using model loaded from disk as per the data.
    pred = np.argmax(model.predict(numpydata))
    print(train['food categories'].unique()[pred])  
    
    
      
    return jsonify(train['food categories'].unique()[pred])
    
    
if __name__ == '__main__':
    app.run(port=5000, debug=True)