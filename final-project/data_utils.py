import torch.utils.data as data
import csv

class CSVData(data.Dataset):

    def __init__(self, csv_file_path='first_model.csv', input_size=2, num_param=1):
        self.points = []
        self.input_size=input_size
        self.num_param=num_param
        with open(csv_file_path) as f:
            reader = csv.reader(f, delimiter=',')
            for row in reader: 
                self.points.append([float(x) for x in row])

    def __getitem__(self, idx):
        point = self.points[idx]
        x = point[0:self.input_size]
        p = point[self.input_size:self.input_size+self.num_param]
        y = point[self.input_size+self.num_param:]
        return torch.FloatTensor(x), torch.FloatTensor(p), torch.FloatTensor(y)

    def __len__(self):
        return len(self.points)