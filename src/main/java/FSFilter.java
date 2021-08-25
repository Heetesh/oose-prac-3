package edu.curtin.comp3003.filesearcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class FSFilter {
    // private BlockingQueue<String> queue = new LinkedBlockingDeque<>();
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    // Thread
    public void put(String fileName) throws RuntimeException {
        try {
            if (fileName != null)
                queue.put(fileName);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed reading a file");
        }
    }

    public String take() throws RuntimeException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed taking from queue");
        }

    }

}
