package pl.dawidziak.model;

import dissimlab.random.SimGenerator;

public class RandomGenerator extends SimGenerator{

    public double generate(Distribution d) throws IllegalArgumentException{
        Number[] params = d.getDistributionParams();
        switch (d.name){
            case exponential:
                return this.exponential(params[0].doubleValue());
            case laplace:
                return this.laplace(params[0].doubleValue());
            case poisson:
                return this.poisson(params[0].doubleValue());
            case geometric:
                return this.geometric(params[0].doubleValue());
            case triangular:
                return this.triangular(params[0].doubleValue());
            case uniform:
                return this.uniform(params[0].doubleValue(), params[1].doubleValue());
            case gamma:
                return this.gamma(params[0].doubleValue(), params[1].doubleValue());
            case normal:
                return this.normal(params[0].doubleValue(), params[1].doubleValue());
            case beta:
                return this.beta(params[0].doubleValue(), params[1].doubleValue());
            case lognormal:
                return this.lognormal(params[0].doubleValue(), params[1].doubleValue());
            case weibull:
                return this.weibull(params[0].doubleValue(), params[1].doubleValue());
            case erlang:
                return this.erlang(params[0].intValue(), params[1].doubleValue());
            case binomial:
                return this.binomial(params[0].doubleValue(), params[1].intValue());
            case negativebinomial:
                return this.negativebinomial(params[0].doubleValue(), params[1].intValue());
            case hyperexponential:
                return this.hyperExponential(params[0].doubleValue(), params[1].doubleValue(), params[2].doubleValue());
            case chisquare:
                return this.chisquare(params[0].intValue());
            case student:
                return  this.student(params[0].intValue());
            case fdistribution:
                return this.fdistribution(params[0].intValue(), params[1].intValue());
            case hypergeometric:
                return this.hypergeometric(params[0].intValue(), params[1].intValue(), params[2].doubleValue());
            default:
                throw new IllegalArgumentException();
        }
    }
}
