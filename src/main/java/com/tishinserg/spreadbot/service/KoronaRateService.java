package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.KoronaRate;
import org.springframework.stereotype.Service;

/**
 * {@link Service} for handling {@link KoronaRate} entity.
 */
public interface KoronaRateService extends RateService {

    /**
     * Save provided {@link KoronaRate} entity.
     *
     * @param koronaRate provided korona rate.
     */
    void save(KoronaRate koronaRate);

    /**
     * Return provided {@link KoronaRate} entity with actual rate from koronapay.com
     */
    KoronaRate getCurrentRate(String countryName, String currencyFrom, String currencyTo);

    KoronaRate getLastRate(String country, String currencyFrom, String currencyTo);


}
