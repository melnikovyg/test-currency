package to;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrencyCursDynamic implements BaseTo{

    private String cursDate;
    private String vCode;
    private String vNom;
    private String vCurs;

    public CurrencyCursDynamic() {
    }

    public String getCursDate() {
        return cursDate;
    }

    public String getvCode() {
        return vCode;
    }

    public String getvNom() {
        return vNom;
    }

    public String getvCurs() {
        return vCurs;
    }

    @Override
    public String toString() {
        return "CurrencyCursDynamic{" +
                "cursDate='" + cursDate + '\'' +
                ", vCode='" + vCode + '\'' +
                ", vNom='" + vNom + '\'' +
                ", vCurs='" + vCurs + '\'' +
                '}';
    }

    @Override
    @JsonIgnore
    public String getVCharCode() {
        return null;
    }
}
