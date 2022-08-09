package com.landauleo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.enterprise.context.ApplicationScoped;

import com.landauleo.exception.HtmlParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class HtmlParser {

    public String parseAndGetPrice(String realtyUrl) {
        try {
            URL url = new URL(realtyUrl);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            log.debug("HTML response by url {}: {}", realtyUrl, response);

            String price = response.toString().replaceFirst(".*?\"price\":(.*?)", "")
                    .replaceAll("(,\"priceM2\":.+)", "").trim();

            log.info("Defined price by url {} realty is {}", realtyUrl, price);

            return price;

        } catch (Exception e) {
            throw new HtmlParseException(String.format("Unable to parse response by url %s, reason: %s", realtyUrl, e));
        }
    }

}
