import matplotlib.pyplot as plt
import torch
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
from scipy.integrate import solve_ivp

from torch.utils.data import DataLoader
from torch.utils.data.sampler import SubsetRandomSampler


def euler(ds, alpha, delta, y):
    u, v = ds(y[0], y[1], alpha)
    return y + delta * np.asarray([u, v])


def plot_phase_portrait(ds, param, model, area, x0):
    Y, X = np.mgrid[area:-area:20j, area:-area:20j]
    U, V = ds(X, Y, param)
    trajectory = []
    y = x0
    for i in range(20000):
        trajectory.append(y)
        y = model(torch.FloatTensor(y).unsqueeze(0), torch.FloatTensor([param]).unsqueeze(0)).tolist()[0]
    trajectory = np.asarray(trajectory).T
    tspan = (0., 200.)
    teval = np.arange(tspan[0], tspan[1], 0.01)
    sol = solve_ivp(lambda t, x: ds(x[0], x[1], param), tspan, x0, t_eval=teval)
    plt.figure(figsize=(5, 5))
    ax = plt.subplot(111)
    ax.streamplot(X, Y, U, V, density=[0.5, 1])
    ax.plot(sol.y[0], sol.y[1])
    ax.plot(trajectory[0], trajectory[1])
    ax.set_title('Alpha: ' + str(param))
    plt.show()
    return


def plot_3d_trajectory(ds, param, model, x0, lw=0.1):
    trajectory = []
    y = x0
    for i in range(20000):
        trajectory.append(y)
        y = model(torch.FloatTensor(y).unsqueeze(0), torch.FloatTensor([param]).unsqueeze(0)).tolist()[0]
    trajectory = np.asarray(trajectory).T
    tspan = (0., 1000.)
    teval = np.arange(tspan[0], tspan[1], 0.02)
    sol = solve_ivp(lambda t, x: ds(x, param), tspan, x0, t_eval=teval)
    fig = plt.figure(figsize=(10, 10))
    ax = fig.add_subplot(111, projection='3d')
    ax.plot(sol.y[0], sol.y[1], sol.y[2], lw=lw)
    ax.plot(trajectory[0], trajectory[1], trajectory[2], lw=lw)
    plt.show()
    return


def train_valid_loader(dataset, bs=100, validation_split=0.2):
    random_seed = 42
    dataset_size = len(dataset)
    indices = list(range(dataset_size))
    split = int(np.floor(validation_split * dataset_size))

    np.random.seed(random_seed)
    np.random.shuffle(indices)

    train_indices, val_indices = indices[split:], indices[:split]

    train_sampler = SubsetRandomSampler(train_indices)
    valid_sampler = SubsetRandomSampler(val_indices)

    train_loader = DataLoader(dataset, batch_size=bs, sampler=train_sampler)
    valid_loader = DataLoader(dataset, batch_size=bs, sampler=valid_sampler)

    return train_loader, valid_loader


def extract_min_max(ds, params, model, x0, axis=0):
    mins_model = []
    maxs_model = []
    mins_true = []
    maxs_true = []
    for param in params:
        trajectory = []
        y = x0
        for i in range(20000):
            trajectory.append(y)
            y = model(torch.FloatTensor(y).unsqueeze(0), torch.FloatTensor([param]).unsqueeze(0)).tolist()[0]

        trajectory = np.asarray(trajectory).T
        tspan = (0., 200.)
        teval = np.arange(tspan[0], tspan[1], 0.01)
        sol = solve_ivp(lambda t, x: ds(x, param), tspan, x0, t_eval=teval)
        mins_model.append(np.min(trajectory[axis][1800:]))
        maxs_model.append(np.max(trajectory[axis][1800:]))
        mins_true.append(np.min(sol.y[axis][1800:]))
        maxs_true.append(np.max(sol.y[axis][1800:]))

    true_min_max = {'mins': mins_true, 'maxs': maxs_true}
    model_min_max = {'mins': mins_model, 'maxs': maxs_model}

    return true_min_max, model_min_max


def plot_bifurcation(min_max, params, true_sys=True, axis='x'):
    mins_true = min_max['mins']
    maxs_true = min_max['maxs']
    plt.figure(figsize=(5,5))
    plt.plot(params, mins_true, 'bo', linestyle='-')
    plt.plot(params, maxs_true, 'ro', linestyle='-', mfc='none', markersize=15)
    title = 'True System' if true_sys else 'Approximated System'
    plt.title(title)
    plt.legend(['min(' + axis + ')', 'max(' + axis + ')'])
    plt.xlabel("a", fontsize=14)
    plt.ylabel(axis, fontsize=14)
    plt.show()
    return
