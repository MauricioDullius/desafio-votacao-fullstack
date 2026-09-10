package br.com.db.system.votingsystem.v1.util;

import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void shouldAcceptValidFutureDateRange() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = start.plusMinutes(5);

        assertDoesNotThrow(() -> DateUtils.validateDates(start, end));
    }

    @Test
    void shouldThrowWhenEndIsBeforeStart() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end = start.minusMinutes(1);

        assertThrows(BusinessRuleException.class, () -> DateUtils.validateDates(start, end));
    }

    @Test
    void shouldThrowWhenStartIsInThePast() {
        LocalDateTime start = LocalDateTime.now().minusMinutes(5);
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);

        assertThrows(BusinessRuleException.class, () -> DateUtils.validateDates(start, end));
    }
}
