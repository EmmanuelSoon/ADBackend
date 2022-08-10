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
    https://sites.google.com/chromium.org/driver/
4. move into the FlaskModel folder 
5. run server.py which is your flask app (localhost:5000)


Steps to start container

Pull image from docker repository [~]$ docker pull gavingyh/ad_ml_model:v1.0

To check if image details (copy the image id): [~]$ docker image list

To create and run container from image on port 5000: [~]$ docker run -p5000:5000 --name ad_backend_flask_arm 

To check if container is running: [~]$ docker ps -a

To stop specific container: [~]$ docker stop <container name/id>

*To exit docker cli -> ^C
