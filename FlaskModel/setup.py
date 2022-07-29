from functions import *

def scrapImages():
    print('scrapping in progress')
    food_list = pd.read_csv('category.csv')
    food_list = food_list['category'].values.tolist()
    print(len(food_list))

    for curr in food_list:
        search_and_download(curr, train_path='./images', test_path='./test_images', train_num= 20, test_num= 10)
        time.sleep(random.uniform(0.5, 1.5))    



if __name__ == '__main__':
    scrapImages()