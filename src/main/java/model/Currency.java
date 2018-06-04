package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import to.BaseTo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currency")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Currency extends BaseEntity implements BaseTo{

    @Column(name = "vcode")
    @NotNull
    private String vCode;

    @JoinColumn(name = "date_time")
    @ManyToOne
    @JsonIgnore
    private DateLoaded date;

    @Column(name = "vcurs")
    private BigDecimal vCurs;

    @Column(name = "vname")
    private String vName;

    @Column(name = "vnom")
    private String vNom;

    @Column(name = "vchcode")
    private String vChCode;

    public Currency(String vCode, DateLoaded date, BigDecimal vCurs, String vName, String vNom, String vChCode) {
        this.vCode = vCode;
        this.date = date;
        this.vCurs = vCurs;
        this.vName = vName;
        this.vNom = vNom;
        this.vChCode = vChCode;
    }

    public Currency() {
    }

    public String getvCode() {
        return vCode;
    }

    public DateLoaded getDate() {
        return date;
    }

    public BigDecimal getvCurs() {
        return vCurs;
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

    @Override
    public String getVCharCode() {
        return null;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "vCode='" + vCode + '\'' +
                ", date=" + date +
                ", vCurs=" + vCurs +
                ", vName='" + vName + '\'' +
                ", vNom='" + vNom + '\'' +
                ", vChCode='" + vChCode + '\'' +
                '}';
    }

}
