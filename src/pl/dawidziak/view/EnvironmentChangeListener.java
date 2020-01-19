package pl.dawidziak.view;

import pl.dawidziak.model.Environment;

public interface EnvironmentChangeListener {
    void reprintEnvironment();
    void setEnvironment(Environment environment);
}
