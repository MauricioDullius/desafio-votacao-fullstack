package br.com.db.system.votingsystem.v1.util;

import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);


    public static void validateDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())) {
            logger.warn("Invalid dates: start={} end={}", start, end);
            throw new BusinessRuleException("Start date cannot be later than the end date or earlier than the current date.");
        }
    }
}
