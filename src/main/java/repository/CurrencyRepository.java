package repository;

import model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query("SELECT c FROM Currency c WHERE c.date.date=?1")
    List<Currency> findByDate(LocalDate date);
}
