package to;

public class CurrencyEnum implements BaseTo{

    private String vCode;
    private String vName;
    private String vEngName;
    private String vNom;
    private String vCommonCode;
    private String vNumCode;
    private String vCharCode;

    public String getvCode() {
        return vCode;
    }

    public String getvName() {
        return vName;
    }

    public String getvEngName() {
        return vEngName;
    }

    public String getvNom() {
        return vNom;
    }

    public String getvCommonCode() {
        return vCommonCode;
    }

    public String getvNumCode() {
        return vNumCode;
    }

    public CurrencyEnum() {
    }

    @Override
    public String toString() {
        return "CurrencyEnum{" +
                "vCode='" + vCode + '\'' +
                ", vName='" + vName + '\'' +
                ", vEngName='" + vEngName + '\'' +
                ", vNom='" + vNom + '\'' +
                ", vCommonCode='" + vCommonCode + '\'' +
                ", vNumCode='" + vNumCode + '\'' +
                ", vCharCode='" + vCharCode + '\'' +
                '}';
    }

    @Override
    public String getVCharCode() {
        return vCharCode;
    }
}
