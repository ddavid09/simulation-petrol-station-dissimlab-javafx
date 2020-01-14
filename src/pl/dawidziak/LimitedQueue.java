package pl.dawidziak;

import pl.dawidziak.model.Client;
import pl.dawidziak.model.Queue;

public class LimitedQueue extends Queue {
    private final int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(Client client){
        if(this.size()<limit){
            super.add(client);
            return true;
        }else{
            return false;
        }

    }
}
