package model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "date_time")
public class DateLoaded extends BaseEntity{

    @Column(name = "date_time")
    private LocalDate date;

    public DateLoaded() {
    }

    public DateLoaded(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

}

