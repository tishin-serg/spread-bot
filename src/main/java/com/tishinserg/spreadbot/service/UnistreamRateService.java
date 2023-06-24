package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.stereotype.Service;

/**
 * {@link Service} for handling {@link UnistreamRate} entity.
 */
public interface UnistreamRateService extends RateService {

    /**
     * Save provided {@link UnistreamRate} entity.
     *
     * @param unistreamRate provided unistream rate.
     */
    void save(UnistreamRate unistreamRate);

    /**
     * Return provided {@link UnistreamRate} entity with actual rate from unistream.com
     */
    UnistreamRate getCurrentRate(String countryName, String currencyFrom, String currencyTo);

    UnistreamRate getLastRate(String country, String currencyFrom, String currencyTo);


}
