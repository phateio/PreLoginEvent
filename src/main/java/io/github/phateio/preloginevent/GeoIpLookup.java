package io.github.phateio.preloginevent;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Looks up country and ASN for an address from the legacy MaxMind GeoIP
 * {@code .dat} databases. The legacy {@link LookupService} is not
 * thread-safe, so lookups are synchronized — fine here, as they run off the
 * main thread in the async pre-login handler.
 */
final class GeoIpLookup implements AutoCloseable {

    private final LookupService countryDb;
    private final LookupService asnDb;

    private GeoIpLookup(LookupService countryDb, LookupService asnDb) {
        this.countryDb = countryDb;
        this.asnDb = asnDb;
    }

    /**
     * Opens both databases, or returns {@code null} (after logging) if either
     * is missing or cannot be read.
     */
    static GeoIpLookup open(File countryFile, File asnFile, Logger logger) {
        if (!countryFile.isFile() || !asnFile.isFile()) {
            logger.warning("GeoIP disabled: database not found (" + countryFile + ", " + asnFile + ")");
            return null;
        }
        try {
            LookupService country = new LookupService(countryFile, LookupService.GEOIP_MEMORY_CACHE);
            LookupService asn = new LookupService(asnFile, LookupService.GEOIP_MEMORY_CACHE);
            return new GeoIpLookup(country, asn);
        } catch (IOException e) {
            logger.warning("GeoIP disabled: could not open databases: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a short "{@code <country>, <asn>}" summary for the address,
     * using "?" for any part that cannot be resolved.
     */
    synchronized String describe(InetAddress address) {
        Country country = countryDb.getCountry(address);
        String code = country == null ? null : country.getCode();
        String asn = asnDb.getOrg(address);
        return clean(code) + ", " + clean(asn);
    }

    private static String clean(String value) {
        return (value == null || value.isEmpty() || value.equals("--")) ? "?" : value;
    }

    @Override
    public synchronized void close() {
        countryDb.close();
        asnDb.close();
    }
}
