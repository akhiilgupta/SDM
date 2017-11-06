import numpy as np
from numpy.linalg import norm
from numpy.linalg import pinv
from numpy.linalg import svd
import matplotlib.pyplot as plt


def selection(matrix, k = 0):
    total_length = np.sum(matrix**2)
    col_length = np.sum(np.square(matrix), axis = 0)
    row_length = np.sum(np.square(matrix), axis = 1)
    prob_col = col_length/total_length
    prob_row = row_length/total_length
    row_select = np.random.choice(matrix.shape[0], k, p=prob_row, replace = False)
    col_select = np.random.choice(matrix.shape[1], k, p=prob_col, replace = False)

    temp_col = k**.5*np.sqrt(prob_col[col_select]).T
    temp_row = k**.5*np.sqrt(prob_row[row_select])
    cols = matrix[:,col_select]/temp_col.T
    rows = matrix[row_select,:]/temp_row[:,None]
    w_matrix = matrix[row_select,:][:,col_select]
    return w_matrix, rows,cols;

def svdUtil(matrix, k=None):
    u_matrix, singular_val, v_matrix = svd(matrix)
    rows = range(k)
    u_matrix = u_matrix[:,rows]
    singular_val = np.diag(singular_val[rows])
    v_matrix = v_matrix[rows,:]
    return u_matrix, singular_val, v_matrix


def errorRecon(ini_matrix, svd_dec):
    construct_matrix = np.dot(svd_dec[0], np.dot(svd_dec[1], svd_dec[2]))
    error_matrix = construct_matrix-ini_matrix
    return np.sum(error_matrix**2)**.5

def curUtil(matrix, k):
    w_matrix, r_norm, c_norm = selection(matrix,k)
    w_inverse = pinv(w_matrix)
    return c_norm, w_inverse, r_norm


if __name__ == "__main__":
    np.set_printoptions(suppress=True,linewidth=np.nan,threshold=np.nan)
    matrix = np.array([
        [1, 1, 1, 0, 0],
        [3, 3, 3, 0, 0],
        [4, 4, 4, 0, 0],
        [5, 5, 5, 0, 0],
        [0, 0, 0, 4, 4],
        [0, 0, 0, 5, 5],
        [0, 0, 0, 2, 2],
    ], dtype = 'float64')
    recon_error_svd = np.empty((0))
    recon_error_cur = np.empty((0))

    for i in range(1,6):
        svd_matrices = svdUtil(matrix, i)
        recon_error_svd = np.append(recon_error_svd,errorRecon(matrix, svd_matrices))
    print(recon_error_svd)
    
    for i in range(1,6):
        cur_matrices = curUtil(matrix, i)
        recon_error_cur = np.append(recon_error_cur,errorRecon(matrix, cur_matrices))
    print(recon_error_cur)

    plt.plot(recon_error_svd)
    plt.plot(recon_error_cur)
    plt.ylabel('Reconstruction Error')
    plt.show()

	