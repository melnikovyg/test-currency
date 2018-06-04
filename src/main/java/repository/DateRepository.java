package repository;

import model.DateLoaded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DateRepository extends JpaRepository<DateLoaded, Integer> {

    @Query("SELECT d FROM DateLoaded d WHERE d.date=?1")
    DateLoaded findByDate(LocalDate date);
}
