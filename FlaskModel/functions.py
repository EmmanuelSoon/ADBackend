import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import os
from keras.preprocessing import image
from keras import models, layers
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
import time
import requests
import io
import hashlib
from PIL import Image
import random



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
def model_training(img_size, img_augmentation, num_classes):
    from keras.applications import EfficientNetB2

    inputs = layers.Input(shape=(img_size, img_size, 3))
    x = img_augmentation(inputs)
    model_EN = EfficientNetB2(include_top=False, input_tensor=x, weights="imagenet")

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
    from keras.models import model_from_json

    # load json and create model
    json_file = open(model_path, 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model = model_from_json(loaded_model_json)
    # load weights into new model
    loaded_model.load_weights(h5_file)

    return loaded_model


def fetch_image_url(query:str, max_num:int, wd:webdriver):
    def scroll_to_end(wd):
        wd.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(random.uniform(1, 2))    

    SEARCH_URL = "https://www.google.com/search?safe=off&site=&tbm=isch&source=hp&q={q}&oq={q}&gs_l=img"
    wd.get(SEARCH_URL.format(q = query))

    image_urls = set()
    image_count = 0
    results_start = 0

    while image_count < max_num:
        scroll_to_end(wd)


        # get all image thumbnail results
        thumbnail_results = wd.find_elements(By.CSS_SELECTOR, 'img.Q4LuWd')
        number_results = len(thumbnail_results)
        
        print(f"Found: {number_results} search results. Extracting links from {results_start}:{number_results}")
        
        for img in thumbnail_results[results_start:number_results]:
            # try to click every thumbnail such that we can get the real image behind it
            try:
                img.click()
                time.sleep(random.uniform(1, 2))    
            except Exception:
                continue

            # extract image urls    
            actual_images = wd.find_elements(By.CSS_SELECTOR, 'img.n3VNCb')
            
            for actual_image in actual_images:
                if actual_image.get_attribute('src') and 'http' in actual_image.get_attribute('src'):
                    image_urls.add(actual_image.get_attribute('src'))

            image_count = len(image_urls)

            if len(image_urls) >= max_num:
                print(f"Found: {len(image_urls)} image links, done!")
                break
        else:
            print("Found:", len(image_urls), "image links, looking for more ...")
            time.sleep(30)
            
            load_more_button = wd.find_element(By.CSS_SELECTOR, '.mye4qd')
            if load_more_button:
                wd.execute_script("document.querySelector('.mye4qd').click();")

        # move the result startpoint further down
        results_start = len(thumbnail_results)

    return image_urls



def persist_img(folder_path, url, name):
    try:
        image_content = requests.get(url).content

    except Exception as e:
        print(f"ERROR - Could not download {url} - {e}")

    try:
        image_file = io.BytesIO(image_content)
        image = Image.open(image_file).convert('RGB')
        file_path = os.path.join(folder_path, name + '_' + hashlib.sha1(image_content).hexdigest()[:10] + '.jpg')
        with open(file_path, 'wb') as f:
            image.save(f, "JPEG", quality=85)
        print(f"SUCCESS - saved {url} - as {file_path}")
    except Exception as e:
        print(f"ERROR - Could not save {url} - {e}")



def search_and_download(query:str, train_path:str , test_path:str , train_num:int , test_num:int ):
    train_folder = os.path.join(train_path,'_'.join(query.lower().split(' ')))
    test_folder = os.path.join(test_path,'_'.join(query.lower().split(' ')))
    number_images = train_num + test_num

    if not os.path.exists(train_folder):
        os.makedirs(train_folder)
    
    if not os.path.exists(test_folder):
        os.makedirs(test_folder)
    
    with webdriver.Chrome(service=Service(ChromeDriverManager().install())) as wd:
        res = fetch_image_url(query, number_images, wd=wd)
        
    counter = 0
    for elem in res:
        if(counter <= train_num):
            persist_img(train_folder ,elem, query.replace(' ', '_'))
        else:
            persist_img(test_folder ,elem, query.replace(' ', '_'))
        counter+=1