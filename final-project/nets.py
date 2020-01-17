import torch
import torch.nn as nn

class RK4(nn.Module):
    def __init__(self, input_size=2, num_param=1 , hidden_size=20, h=1):
        super(RK4, self).__init__()
        self.lin1 = nn.Linear(input_size + num_param, hidden_size, bias=True)
        self.lin2 = nn.Linear(hidden_size, hidden_size, bias=True)
        self.lin3 = nn.Linear(hidden_size, input_size, bias=True)
        self.h = h

    def forward(self, x, p):
        i = torch.cat((x,p), 1)
        k1 = self.h * self.lin3(torch.sigmoid(self.lin2(torch.relu(self.lin1(i)))))
        i = torch.cat((x + 0.5 * k1,p), 1)
        k2 = self.h * self.lin3(torch.sigmoid(self.lin2(torch.relu(self.lin1(i)))))
        i = torch.cat((x + 0.5 * k2,p), 1)
        k3 = self.h * self.lin3(torch.sigmoid(self.lin2(torch.relu(self.lin1(i)))))
        i = torch.cat((x + k3,p), 1)
        k4 = self.h * self.lin3(torch.sigmoid(self.lin2(torch.relu(self.lin1(i)))))
        output = x + (k1 + 2 * k2 + 2 * k3 + k4) / 6
        return output


    def save(self, path):
        """
        Save model with its parameters to the given path. Conventionally the
        path should end with "*.model".

        Inputs:
        - path: path string
        """
        print('Saving model... %s' % path)
        torch.save(self, path)


class Euler(nn.Module):
    def __init__(self, input_size=2, num_param=1 , hidden_size=20, h=1):
        super(Euler, self).__init__()
        self.lin1 = nn.Linear(input_size + num_param, hidden_size, bias=True)
        self.lin2 = nn.Linear(hidden_size, hidden_size, bias=True)
        self.lin3 = nn.Linear(hidden_size, input_size, bias=True)
        self.h = h

    def forward(self, x, p):
        i = torch.cat((x,p), 1)
        f = self.lin3(torch.relu(self.lin2(torch.relu(self.lin1(i)))))
        output = x + self.h * f
        return output


    def save(self, path):
        """
        Save model with its parameters to the given path. Conventionally the
        path should end with "*.model".

        Inputs:
        - path: path string
        """
        print('Saving model... %s' % path)
        torch.save(self, path)
        