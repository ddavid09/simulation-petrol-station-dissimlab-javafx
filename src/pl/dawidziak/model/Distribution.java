package pl.dawidziak.model;

public class Distribution {
    public final DistributionName name;
    private Number[] distributionParams;

    public Distribution(DistributionName name, Number... distributionParams) {
        this.name = name;
        this.distributionParams = distributionParams;
    }


    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        params.append(name.toString());
        params.append("(");
        for(int i=0; i<distributionParams.length; i++){
            params.append(distributionParams[i].doubleValue());
            if(i < distributionParams.length-1){
                params.append(", ");
            }else{
                params.append(")");
            }
        }
        return params.toString();
    }

    public Number[] getDistributionParams() {
        return distributionParams;
    }

    public void setDistributionParams(Number[] distributionParams) {
        this.distributionParams = distributionParams;
    }
}
