package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FSFileFinder {
    private String searchPath;
    private String searchTerm;
    private FSUserInterface ui;
    private FSFilter filter; // Blocking queue'ing class.
    private Thread finderThread;
    private Thread filterThread;

    public FSFileFinder(String searchPath, String searchTerm, FSUserInterface ui) {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.ui = ui;
        filter = new FSFilter(); // FSFilter class
    }

    public void search() {
        Thread t = new Thread(() -> {
            try {
                // Recurse through the directory tree
                Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        // Check whether each file contains the search term, and if
                        // so, add it to the list.

                        String fileStr = file.toString();
                        finderThread = new Thread(() -> {
                            filter.put(fileStr);
                        }, "finder-thread");

                        finderThread.start();

                        // Takes from the queue and searches if it contains the term.
                        filterThread = new Thread(() -> {
                            String search;
                            search = filter.take();

                            if (search.contains(searchTerm)) {
                                ui.addResult(fileStr);
                            }

                        }, "filter-thread");

                        filterThread.start();

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException | RuntimeException e) {
                // This error handling is a bit quick-and-dirty, but it will suffice here.
                ui.showError(e.getClass().getName() + ": " + e.getMessage());
            }

        });

        t.start();
    }
}
