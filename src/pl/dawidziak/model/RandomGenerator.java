package pl.dawidziak.model;

import dissimlab.random.SimGenerator;

public class RandomGenerator extends SimGenerator{

    public double generate(Distribution d) throws IllegalArgumentException{
        Number[] params = d.getDistributionParams();
        switch (d.name){
            case exponential:
                return this.exponential(params[0].doubleValue());
            case poisson:
                return this.poisson(params[0].doubleValue());
            case geometric:
                return this.geometric(params[0].doubleValue());
            case uniform:
                return this.uniform(params[0].doubleValue(), params[1].doubleValue());
            case gamma:
                return this.gamma(params[0].doubleValue(), params[1].doubleValue());
            case normal:
                return Math.abs(this.normal(params[0].doubleValue(), params[1].doubleValue()));
            case beta:
                return this.beta(params[0].doubleValue(), params[1].doubleValue());
            case lognormal:
                return this.lognormal(params[0].doubleValue(), params[1].doubleValue());
            case weibull:
                return this.weibull(params[0].doubleValue(), params[1].doubleValue());
            case erlang:
                return this.erlang(params[0].intValue(), params[1].doubleValue());
            case chisquare:
                return this.chisquare(params[0].intValue());
            case student:
                return  Math.abs(this.student(params[0].intValue()));
            case fdistribution:
                return this.fdistribution(params[0].intValue(), params[1].intValue());
            default:
                throw new IllegalArgumentException();
        }
    }
}
