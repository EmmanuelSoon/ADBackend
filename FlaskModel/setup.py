from functions import *

def scrapImages():
    curr_dir = os.path.dirname(os.path.realpath(__file__))
    print('scrapping in progress')
    food_list = pd.read_csv(os.path.join(curr_dir,'category.csv'))
    food_list = food_list['0'].values.tolist()
    print(len(food_list))


    for curr in food_list:
        search_and_download(curr, train_path= os.path.join(curr_dir,'images'), test_path=os.path.join(curr_dir,'test_images'), train_num= 20, test_num= 10)
        time.sleep(random.uniform(0.5, 1.5))    



if __name__ == '__main__':
    scrapImages()