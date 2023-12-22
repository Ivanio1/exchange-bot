package ivanio1.exchangebot.service;

import ivanio1.exchangebot.client.CbrClient;
import ivanio1.exchangebot.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.swing.text.Document;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Autowired
    private CbrClient client;

    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml,USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml,EUR_XPATH);
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Не удалось распарсить XML.", e);
        }
    }
}
