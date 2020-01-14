package pl.dawidziak.model;

public class Client {

    public final int idNumber;
    private ClientType type;
    private FuelType fuelType;

    public Client(int id) {
        idNumber = id;
    }

    public void DrawLotClientType(RandomGenerator randGen, Distribution distribution) {
        double onlyFuel = randGen.generate(distribution);
        double onlyWash = randGen.generate(distribution)/2;
        double fuelWash = randGen.generate(distribution);
        if(fuelWash > onlyFuel && fuelWash > onlyWash){
            this.type = ClientType.FUEL_WASH;
        }else if(onlyWash > onlyFuel && onlyWash > fuelWash){
            this.type = ClientType.ONLY_WASH;
        }else{
            this.type = ClientType.ONLY_FUEL;
        }
    }

    public void DrawLotFuelType(RandomGenerator randGen, Distribution PBdistribution, Distribution ONdistribution, Distribution LPGdistribution) {
        double pb = randGen.generate(PBdistribution);
        double on = randGen.generate(ONdistribution);
        double lpg = randGen.generate(LPGdistribution);
        if(pb > on && pb > lpg){
            this.fuelType = FuelType.PB;
        }else if(on > pb && on > lpg){
            this.fuelType = FuelType.ON;
        }else{
            this.fuelType = FuelType.LPG;
        }
    }

    public ClientType getClientType() {
        return type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public String infoToString(){
        StringBuilder sb = new StringBuilder("[");
        switch (type){
            case ONLY_WASH:
                sb.append("tylko myjnia");
                break;
            case FUEL_WASH:
                sb.append("tankowanie + myjnia");
                break;
            case ONLY_FUEL:
                sb.append("tylko tankowanie");
        }
        if(fuelType != null){
            sb.append(", ");
            switch(fuelType){
                case ON:
                    sb.append("ON");
                    break;
                case PB:
                    sb.append("PB");
                    break;
                case LPG:
                    sb.append("LPG");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
