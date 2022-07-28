Hello Users of the flask app.

For first time setups please do the following 

1. Go into your the python environment of your choice 
2. Ensure that the following libraries are installed. 
    - Selenium 
    - Keras 
    - Tensorflow 
    - sklearn
    - numpy
    - pandas
    - webdriver_manager 

3. download the chromedriver.exe according to the your chrome ver. and put into into the FlaskModel Folder 
4. move into the FlaskModel folder 
5. run the setup.py which will scrape the web for the images that will be used in your model training in the next step (cmd: python setup.py) 
6. run model.py which will train the CNN model to be used in the flask app to do the predictions (cmd: python model.py)
7. run server.py which is your flask app (localhost:5000)
