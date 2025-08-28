package handlers;

import model.Monster;
import java.io.File;
import java.util.List;

public interface Handler {
    void setNext(Handler handler);
    List<Monster> handleRequest(File file);
    boolean canHandle(File file);
    void exportData(List<Monster> monsters, File file);
}