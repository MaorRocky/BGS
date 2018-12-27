package bgu.spl.net.srv;

public class IdCounter {
    private static class SingletonHolder {
        private static IdCounter instance = new IdCounter();
    }
    private int nextId = 0;

    public IdCounter getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized int getNextId() {
        int toReturn = nextId;
        nextId++;
        return toReturn;
    }
}
