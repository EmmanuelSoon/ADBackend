import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import os
from keras.preprocessing import image
from keras import models, layers
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split

# Creating the Dataframes from the directories
def create_df(dir_name):
    food_cat = []
    food_img = []

    for i in os.listdir(dir_name):
        for image_filename in os.listdir(dir_name + i):
            food_cat.append(i)
            food_img.append(i + '/' + image_filename)
    
    df = pd.DataFrame(food_cat, columns=['food categories'])
    df['image'] = food_img

    return df

# converting images into np.arrays 
def convert_img(df, dir_name, img_size):
    img_list = None
    for img_url in df:
        img = image.load_img(dir_name + img_url, target_size=(img_size,img_size)).convert('RGB')
        if img_list is None:
            img_list = img
        else:
            img_list = np.concatenate((img_list, img))
    
    img_list = img_list.reshape(-1, img_size, img_size, 3)
   
    return img_list


# B0 efficient Net Model 
def B0model(img_size, img_augmentation, num_classes):
    from tensorflow.keras.applications import EfficientNetB0

    inputs = layers.Input(shape=(img_size, img_size, 3))
    x = img_augmentation(inputs)
    model_EN = EfficientNetB0(include_top=False, input_tensor=x, weights="imagenet")

    # Freeze the pretrained weights
    model_EN.trainable = False

    # Rebuild top
    x = layers.GlobalAveragePooling2D(name="avg_pool")(model_EN.output)
    x = layers.BatchNormalization()(x)

    top_dropout_rate = 0.2
    x = layers.Dropout(top_dropout_rate, name="top_dropout")(x)
    outputs = layers.Dense(num_classes, activation="softmax", name="pred")(x)


    model_EN = tf.keras.Model(inputs, outputs, name="EfficientNet")
    model_EN.compile(
        optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"]
    )
    return model_EN


# Saving Model as Json format
def SaveModel(model):
    # Saving Model into disk 
    model_json = model.to_json()
    with open("model.json", "w") as json_file:
        json_file.write(model_json)

    # serialize weights to HDF5
    model.save_weights("model.h5")


# Load saved model 
def loadModel(model_path, h5_file):
    from tensorflow.keras.models import model_from_json

    # load json and create model
    json_file = open(model_path, 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model = model_from_json(loaded_model_json)
    # load weights into new model
    loaded_model.load_weights(h5_file)
