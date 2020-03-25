package com.github.cookzhang.ais.queue.circle;


import java.io.Serializable;
import java.util.ResourceBundle;

public class CircularQueue<E> implements Serializable {
    private E data[];
    private int head;
    private int tail;

    public static CircularQueue getInstance() {
        return QueueHolder.instance;
    }

    private static class QueueHolder {
        private static CircularQueue instance = new CircularQueue();
    }

    @SuppressWarnings("unchecked")
    public CircularQueue() {
        data = (E[]) new Object[Integer.parseInt(ResourceBundle.getBundle("queue").getString("circular.queue.size"))];
        head = 0;
        tail = 0;
    }

    public boolean push(E value) {
        if (!bufferFull()) {
            data[tail++] = value;
            if (tail == data.length) {
                tail = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public E poll() {
        if (head != tail) {
            E value = data[head++];
            if (head == data.length) {
                head = 0;
            }
            return value;
        } else {
            return null;
        }
    }

    private boolean bufferFull() {
        return tail + 1 == head || tail == (data.length - 1) && head == 0;
    }
}
