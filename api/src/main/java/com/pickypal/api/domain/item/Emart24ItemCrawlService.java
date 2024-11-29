package com.pickypal.api.domain.item;

import com.pickypal.api.domain.item.Item;
import com.pickypal.api.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class Emart24ItemCrawlService {
    private final ItemRepository iRepo;

    // 한 페이지 단위로 크롤링하여 List 형태로 역직렬화
    private List<List<String>> fetchEventItemSinglePage(int page) throws IOException {
        final String URL = "https://emart24.co.kr/goods/event?search=&page=" + page + "&category_seq=&align=";
        Document doc = Jsoup.connect(URL).get();
        Elements elements = doc.select(".itemWrap");
        // 요청을 보낸 해당 page에 item이 없을 경우, elements는 [] (빈 리스트)

        if (elements.size() == 0) {
            return null;
        }

        log.info("* * * fetchSinglePage: Original data");
        for(Element e : elements) {
            System.out.println(e.text());
        }

        // Elements -> List<List<String>>
        List<List<String>> eList = new ArrayList<>();
        for(Element e : elements) {
            String eStr = e.text();
            eStr = eStr.replace("NEW ", "");
            eStr = eStr.replaceAll(",", "");
            eStr = eStr.replaceAll(" 원", "");
            eStr = eStr.replace("1 + 1 ", "1+1 ");
            eStr = eStr.replace("2 + 1 ", "2+1 ");
            eList.add(Arrays.asList(eStr.split(" ")));
        }

        log.info("* * * fetchSinglePage: Parsed data");
        System.out.println(eList.toString());

        return eList;
    }

    // List를 DB에 저장
    public void saveEventItems() throws IOException {

        // fetchEventItemSinglePage의 return이 null일 경우, fetch 종료
        int page = 1;
        long itemNo = 1L;
        while (true) {
            try {
                List<List<String>> eList = fetchEventItemSinglePage(page++);
                if (eList == null) break;
                List<Item> entities = new ArrayList<>();

                for (List<String> e : eList) {
                    Item item = new Item();
                    String name = e.get(1);

                    // e.get(2)가 int로 파싱되지 않으면 e.get(1)과 space 두고 concat
                    int parsedPrice = -1;
                    try {
                        parsedPrice = Integer.parseInt(e.get(2));

                    } catch (Exception ex) {
                        // concat 필요, e.get(3)가 price가 됨
                        name = name.concat(" " + e.get(2));
                        parsedPrice = Integer.parseInt(e.get(3));
                    }

                    // event item은 납품처명 다 있음
                    String[] splitted = name.split("\\)", 2); // 한 번만 split
                    String supplierName;

                    if (splitted.length == 1) {
                        supplierName = null;
                        name = splitted[0];
                    }
                    else {
                        supplierName = splitted[0];
                        name = splitted[1];
                    }

                    String iid = "EVENT_" + String.format("%05d", itemNo);
                    String tag = e.get(0);

                    log.info("* * * fetchSinglePage: entity data");
                    System.out.println("iid: " + iid);
                    System.out.println("supplier: " + supplierName);
                    System.out.println("name: " + name);
                    System.out.println("price: " + parsedPrice);
                    System.out.println("tag: " + tag);

                    item.setId(iid);
                    item.setSupplierName(supplierName);
                    item.setName(name);
                    item.setPrice(parsedPrice);
                    item.setTag(tag);
                    entities.add(item);
                    itemNo += 1;
                }
                iRepo.saveAll(entities);
            } catch (Exception exception) {
                // drop row: 강사님이 자잘한 예외는 버리라고 하셨음
                exception.printStackTrace();
            }
        }

        log.info("* * * saveItems: Saved all");
    }
}
