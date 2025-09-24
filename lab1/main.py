import random
from concurrent.futures import ProcessPoolExecutor as Pool
from statistics import median,stdev


def get_path(i):
    return f'lab1/file{i}.csv'

def create_files(n: int) :
    categories = ['A','B','C','D']
    for i in range(0,n):
        fp = open(get_path(i), 'w')
        for j in range(20):
            cat = categories[random.randint(0,3)]
            rf = random.random()
            fp.write(f'{cat} {rf}\n')
        fp.close()

# def median(t):
#     if not t:
#         return 0
#     u=sorted(t)
#     mid = int(len(u)/2)
#     if len(u)%2==0:
#         return((u[mid-1]+u[mid])/2)
#     else:
#         return(u[mid])

# def standart_deviation(list):
#     if not list or len(list) < 2:
#          return 0
#     m = sum(list) / len(list)
#     e = 0
#     for i in list:
#         e += (i - m)**2
#     e /= len(list)
#     return e**0.5

def fill_from_file(fp,cats):
    list = fp.readlines()
    for i in list:
        cat,fr = i.split(' ')
        ff = float(fr)
        cats.get(cat).append(ff)

def medians_sd_from_files(i):
    path = get_path(i)
    res = {'A':0,'B':0,'C':0,'D':0}
    fp = open(path, 'r')
    cats = {'A':[],'B':[],'C':[],'D':[]}
    fill_from_file(fp,cats)
    print(path)
    for key, value in cats.items():
        med = median(value)
        res[key] = med
        if (len(value) == 1):
            print(f'{key} med: {med} stdev: 0')
        else:
            print(f'{key} med: {med} stdev: {stdev(value)}')
    fp.close
    print
    return res

n = 5
create_files(n)
x2msb = {'A':[],'B':[],'C':[],'D':[]}

if __name__ == '__main__':
    with Pool(max_workers=n) as pool:
        try:
            results = pool.map(medians_sd_from_files, range(n))
            for res in results:
                for key, value in res.items():
                    x2msb[key].append(value)
            print("all median and standart deviation:")
            for key, value in x2msb.items():
                if value:
                    print(f'{key} med: {median(value)} stdev: {stdev(value)}')
                else:
                    print(f'{key} нет данных')
                    
        except Exception as e:
            print(f"Ошибка: {e}")