package com.serabile.data.util

import java.util.Locale
import javax.inject.Inject

/**
 * Provides the device locale for API requests
 */
class LocaleProvider @Inject constructor() {

    fun getCountryCode(): String {
        val countryCode = Locale.getDefault().country.lowercase()

        // NewsAPI supports limited country codes
        val supportedCountries = listOf(
            "ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn",
            "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu",
            "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma",
            "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro",
            "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw",
            "ua", "us",
        )

        return if (countryCode in supportedCountries) countryCode else "us"
    }
}
