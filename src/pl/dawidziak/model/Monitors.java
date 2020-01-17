package pl.dawidziak.model;

import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.SimManager;

public class Monitors {
    public final MonitoredVar sizeQueueFuel;
    public final MonitoredVar sizeQueueWash;
    public final MonitoredVar serviceTime;
    public final MonitoredVar washTime;
    public final MonitoredVar lostClient;

    public Monitors(SimManager simManager) {
        this.sizeQueueFuel = new MonitoredVar(simManager);
        this.sizeQueueWash = new MonitoredVar(simManager);
        this.serviceTime = new MonitoredVar(simManager);
        this.washTime = new MonitoredVar(simManager);
        this.lostClient = new MonitoredVar(simManager);
    }
}
