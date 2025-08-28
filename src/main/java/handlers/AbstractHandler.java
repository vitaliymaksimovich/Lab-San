package handlers;

import model.Monster;
import java.io.File;
import java.util.List;

public abstract class AbstractHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler handler) {
        this.next = handler;
    }

    @Override
    public List<Monster> handleRequest(File file) {
        if (canHandle(file)) {
            return processFile(file);
        } else if (next != null) {
            return next.handleRequest(file);
        }
        return null;
    }

    protected abstract List<Monster> processFile(File file);

    @Override
    public void exportData(List<Monster> monsters, File file) {
        if (canHandle(file)) {
            writeFile(monsters, file);
        } else if (next != null) {
            next.exportData(monsters, file);
        }
    }

    protected abstract void writeFile(List<Monster> monsters, File file);
}