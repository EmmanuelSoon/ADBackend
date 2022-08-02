from genericpath import isfile

import numpy as np 
from flask import Flask, request, jsonify
from functions import *
import base64
import io
import PIL.Image as Image
import tensorflow as tf
import setup
import model

curr_dir = os.path.dirname(os.path.realpath(__file__))

# train_dir = 'FlaskModel/images/'
train_dir = os.path.join(curr_dir, 'images/')

app = Flask(__name__)

# Load the model
def compileModel():
    loaded_model = loadModel(os.path.join(curr_dir,'model.json'), os.path.join(curr_dir,'model.h5'))
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
    print(type(img))
    from numpy import asarray

    #resized = tf.image.resize(img,(260,260))
    resized = img.resize((260,260))


    numpydata = asarray(resized, dtype=int) 
    numpydata = np.expand_dims(numpydata, 0)
    numpydata = np.expand_dims(numpydata, -1)

    # Make prediction using model loaded from disk as per the data.
    pred = np.argmax(loaded_model.predict(numpydata))
    print(np.sort(train['food categories'].unique()))  
    print(np.sort(train['food categories'].unique())[pred])  
      
    return jsonify(np.sort(train['food categories'].unique())[pred])
    
    

if __name__ == '__main__':


    print(__file__)
    print(os.path.dirname(__file__))
    print(os.path.dirname(os.path.realpath(__file__)))
    print(os.getcwd())

    image_folder = os.path.join(curr_dir,'images')
    test_images = os.path.join(curr_dir,'test_images')
    model_file = os.path.join(curr_dir,'model.json')
    model_h5 = os.path.join(curr_dir,'model.h5')


    # image_folder = 'FlaskModel/images'
    # test_images = 'FlaskModel/test_images'
    # model_file = 'FlaskModel/model.json'
    # model_h5 = 'FlaskModel/model.h5'

    if not os.path.isdir(image_folder):
        print('Image folders not found, scraping the web for images now...Please wait')
        setup.scrapImages()
    
    if not os.path.isfile(model_file) and not os.path.isfile(model_h5):
        print('Model not found, training model now... Please wait')
        model.buildModel()
    
    train, loaded_model = compileModel()
    app.run(port=5000, debug=True)

    