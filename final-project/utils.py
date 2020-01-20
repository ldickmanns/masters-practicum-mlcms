import matplotlib.pyplot as plt
import torch
import numpy as np


def euler(ds, alpha, delta, y):
    u, v = ds(y[0], y[1], alpha)
    return y + delta * np.asarray([u, v])


def plot_phase_portrait(ds, param, model, area):
    Y, X = np.mgrid[area:-area:20j, area:-area:20j]
    U, V = ds(X, Y, param)
    trajectory1 = []
    trajectory2 = []
    y1 = [area, -area]
    y2 = [area, -area]
    for i in range(10000):
        trajectory1.append(y1)
        y1 = euler(ds, param, 0.001, y1)
        trajectory2.append(y2)
        y2 = model(torch.FloatTensor(y2).unsqueeze(0), torch.FloatTensor([param]).unsqueeze(0)).tolist()[0]
    trajectory1 = np.asarray(trajectory1).T
    trajectory2 = np.asarray(trajectory2).T
    plt.figure(figsize=(5, 5))
    ax = plt.subplot(111)
    ax.streamplot(X, Y, U, V, density=[0.5, 1])
    ax.plot(trajectory1[0], trajectory1[1])
    ax.plot(trajectory2[0], trajectory2[1])
    ax.set_title('Alpha: ' + str(param))
    plt.show()
    return