import numpy as np


def linear_system(x1, x2, a):
    dx1 = -a * (x1 + x2)
    dx2 = 0.25 * x1
    return dx1, dx2


def vec_linear_system(x, a):
    x1 = x[0]
    x2 = x[1]
    return np.array(linear_system(x1, x2, a))


def andronov_hopf(x1, x2, a):
    dx1 = a * x1 - x2 - x1 * (x1 ** 2 + x2 ** 2)
    dx2 = x1 + a * x2 - x2 * (x1 ** 2 + x2 ** 2)
    return dx1, dx2


def vec_andronov_hopf(x, a):
    x1 = x[0]
    x2 = x[1]
    return np.array(andronov_hopf(x1, x2, a))


def roessler_attractor(x, y, z, a):
    dx = -y - z
    dy = x + a * y
    dz = 2 + z * (x - 4)
    return dx, dy, dz


def vec_roessler_attractor(x, a):
    x = x[0]
    y = x[1]
    z = x[2]
    return np.array(roessler_attractor(x, y, z, a))