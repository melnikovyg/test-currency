package to;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrencyCursOnDay implements BaseTo {

    private String vName;
    private String vNom;
    private String vCurs;
    private String vCode;
    private String vChCode;

    public CurrencyCursOnDay() {
    }

    public String getvCurs() {
        return vCurs;
    }

    @Override
    public String toString() {
        return "CurrencyCursOnDay{" +
                "vName='" + vName + '\'' +
                ", vNom='" + vNom + '\'' +
                ", vCurs='" + vCurs + '\'' +
                ", vCode='" + vCode + '\'' +
                ", vChCode='" + vChCode + '\'' +
                '}';
    }

    @Override
    @JsonIgnore
    public String getVCharCode() {
        return vChCode;
    }

    public String getvCode() {
        return vCode;
    }

    public String getvName() {
        return vName;
    }

    public String getvNom() {
        return vNom;
    }

    public String getvChCode() {
        return vChCode;
    }
}
