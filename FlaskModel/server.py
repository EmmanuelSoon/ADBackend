from genericpath import isfile
from tkinter import Image
import numpy as np 
from flask import Flask, request, jsonify
from functions import *
import base64
import io
import PIL.Image as Image
import tensorflow as tf
import setup
import model


train_dir = './images/'


app = Flask(__name__)

# Load the model
def compileModel():
    loaded_model = loadModel('model.json', 'model.h5')
    loaded_model.compile(
        optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"]
    )
    train = create_df(train_dir)
    return train, loaded_model


@app.route('/predict_api',methods=['POST'])
def predict():
    print('test')
    # Get the data from the POST request.
    data = request.get_data()
    # data = request.get_json(force=True) 

    # convert json string back into img   
    img = Image.open(io.BytesIO(data))
    from numpy import asarray
    resized = tf.image.resize(img, (260,260))

    numpydata = asarray(resized, dtype=int) 
    numpydata = np.expand_dims(numpydata, 0)
    numpydata = np.expand_dims(numpydata, -1)

    # Make prediction using model loaded from disk as per the data.
    pred = np.argmax(loaded_model.predict(numpydata))
    print(train['food categories'].unique()[pred])  
    
    
      
    return jsonify(train['food categories'].unique()[pred])
    
    

if __name__ == '__main__':

    image_folder = './images'
    test_images = './test_images'
    model_file = './model.json'
    model_h5 = './model.h5'

    if not os.path.isdir(image_folder) and not os.path.isdir(test_images):
        print('Image folders not found, scraping the web for images now...Please wait')
        setup.scrapImages()
    
    if not os.path.isfile(model_file) and not os.path.isfile(model_h5):
        print('Model not found, training model now... Please wait')
        model.buildModel()
    
    train, loaded_model = compileModel()
    app.run(port=5000, debug=True)

    