import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import os
from keras.preprocessing import image
from keras import models, layers
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split

from functions import *

def buildModel():
    # Train and test dataframes
    curr_dir = os.path.dirname(os.path.realpath(__file__))
    train_dir = os.path.join(curr_dir, 'images/')
    test_dir = os.path.join(curr_dir, 'test_images/')

    train = create_df(train_dir)
    test = create_df(test_dir)

    # Constants 
    img_size = 224 
    epoch = 50

    img_augmentation = models.Sequential(
        [
            layers.RandomRotation(factor=0.2, fill_mode='nearest'),
            layers.RandomTranslation(height_factor=0.1, width_factor=0.1),
            layers.RandomFlip(),
            layers.RandomContrast(factor=0.1),
        ],
    )
    num_classes = len(train['food categories'].unique())


    ##----------------------------------------- Training the model -----------------------------------------------------##
    print('loading dataset...')
    train_img = convert_img(train["image"], train_dir, img_size)
    test_img = convert_img(test["image"], test_dir, img_size)

    # one hot encoding 
    cat_train = OneHotEncoder().fit_transform(train[["food categories"]]).toarray()
    cat_test = OneHotEncoder().fit_transform(test[["food categories"]]).toarray()
    x_train, x_val, y_train, y_val = train_test_split(train_img, cat_train, random_state =99, test_size=0.1)

    print('training model in progress...')
    #Using B0 model
    model_EN = model_training(260, img_augmentation, num_classes)
    hist_EN = model_EN.fit(x_train, y_train, epochs=epoch, verbose=2, validation_data=(x_val, y_val), shuffle=True)

    #Saving the model as JSON format 
    SaveModel(model_EN)


if __name__ == '__main__':
    buildModel()


