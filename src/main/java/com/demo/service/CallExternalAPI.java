package com.demo.service;

import com.demo.model.CurrencyEntity;
import com.demo.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

@Component("example")
public class CallExternalAPI {
    public static final String LITHUANIAN_BANK_SITE_WITH_DATE = "http://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRates?tp=eu&dt=";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String FX_RATE_TAG = "FxRate";
    public static final String START_DATE = "2020-09-05";
    public static final String DATA_TAG = "Dt";
    public static final String AMOUNT_TAG = "Amt";
    public static final String CURRENCY_TAG = "Ccy";
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CallExternalAPI(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @PostConstruct
    public void makeRequestAndParseDataToDatabase() throws ParserConfigurationException, IOException, SAXException {
        LocalDate start = LocalDate.parse(START_DATE, dataFormatter);
        LocalDate end = LocalDateTime.now()
                                     .toLocalDate();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Document document = getParse(builder, date);
            parseCurrenciesToDatabase(document);
        }
    }

    private Document getParse(DocumentBuilder builder, LocalDate date) throws SAXException, IOException {
        Document document = builder.parse(LITHUANIAN_BANK_SITE_WITH_DATE + date);
        document.getDocumentElement()
                .normalize();
        return document;
    }

    private void parseCurrenciesToDatabase(Document document) {
        NodeList nodeList = document.getElementsByTagName(FX_RATE_TAG);

        int nodeListLength = nodeList.getLength();
        IntStream.range(0, nodeListLength)
                 .mapToObj(nodeList::item)
                 .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                 .map(node -> (Element) node)
                 .map(this::createCurrency)
                 .filter(this::checkRecordInDatabase)
                 .forEach(currencyRepository::save);
    }

    private boolean checkRecordInDatabase(CurrencyEntity currencyEntity) {
        return currencyRepository.findByNameAndRatioAndDate(currencyEntity.getName(), currencyEntity.getRatio(), currencyEntity.getDate())
                                 .isEmpty();
    }

    private CurrencyEntity createCurrency(Element element) {
        String currencyName = getItemByTagNameAndIndex(element, CURRENCY_TAG, 1);
        double currencyCourse = Double.parseDouble(getItemByTagNameAndIndex(element, AMOUNT_TAG, 1));
        String dateWhenTaken = getItemByTagNameAndIndex(element, DATA_TAG, 0);
        LocalDate convertedDataFromString = LocalDate.parse(dateWhenTaken, dataFormatter);
        return new CurrencyEntity(currencyName, currencyCourse, convertedDataFromString);
    }

    private String getItemByTagNameAndIndex(Element element, String tageName, int index) {
        return element.getElementsByTagName(tageName)
                      .item(index)
                      .getTextContent();
    }
}
