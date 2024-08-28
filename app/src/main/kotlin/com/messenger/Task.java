package com.messenger;

public interface Task<T> {
    void success(T result);
}

