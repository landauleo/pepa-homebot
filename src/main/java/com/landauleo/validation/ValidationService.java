package com.landauleo.validation;

import java.math.BigInteger;
import javax.enterprise.context.ApplicationScoped;

import com.landauleo.exception.HtmlParseException;
import com.landauleo.exception.RealtyUrlException;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class ValidationService {

    //not sponsored :D
    public static final String MIR_KVARTIR_URL = "https://www.mirkvartir.ru/";

    public void validateUrl(String url) {
        String realtyId = url.replaceFirst(".*?" + MIR_KVARTIR_URL + "(.*?)", "")
                .replaceAll("/", "").trim();

        if (StringUtils.isBlank(realtyId) || !realtyId.matches("\\d+")) {
            throw new RealtyUrlException("Invalid url exception. Numeric characters expected after " + MIR_KVARTIR_URL);
        }
    }

    public void validatePrice(String price) {
        if (StringUtils.isEmpty(price) || !price.matches("\\d+")) {
            throw new HtmlParseException("Defined price is invalid");
        }
        try {
            new BigInteger(price);
        } catch (NumberFormatException  e) {
            throw new HtmlParseException("Defined price is invalid");
        }
    }

}
