package com.tishinserg.spreadbot.service;

import com.tishinserg.spreadbot.repository.entity.UnistreamRate;
import org.springframework.stereotype.Service;

/**
 * {@link Service} for handling {@link UnistreamRate} entity.
 */
public interface UnistreamRateService {

    /**
     * Save provided {@link UnistreamRate} entity.
     *
     * @param unistreamRate provided unistream rate.
     */
    void save(UnistreamRate unistreamRate);

    /**
     * Return provided {@link UnistreamRate} entity last record.
     */
    UnistreamRate findLastRate();

    /**
     * Return provided {@link UnistreamRate} entity with actual rate from unistream.com
     */
    UnistreamRate getActualRate();


}
