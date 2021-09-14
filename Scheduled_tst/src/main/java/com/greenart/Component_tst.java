package com.greenart;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.greenart.utils.DateUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Component
public class Component_tst {
    @Scheduled(cron="0 */1 * * * *")
    public void getCoronaStatus() throws Exception {

        // String aa = DateUtils.makeAWeekAgoDateString();
        // String bb = DateUtils.makeTodayString();

        StringBuilder builder = new StringBuilder("http://opendata.kwater.or.kr/openapi-data/service/pubd/waterpedia/knwldgbank/list"); /*URL*/
        builder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D"); /*Service Key*/
        builder.append("&" + URLEncoder.encode("searchText","UTF-8") + "=" + URLEncoder.encode("논문", "UTF-8")); /*논문*/
        builder.append("&" + URLEncoder.encode("category","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*0:전체, 1:구분, 2:제목, 3:출처*/
        builder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지당 줄수*/
        builder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(builder.toString());
        // System.out.println(builder.toString());

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
        // System.out.println(nList.getLength());
        String fileName = "kor_scs"+DateUtils.makeTodayString()+".txt";
        String filePath = "/home/Korea/Kor/"+fileName;
        // ture - append/ false - overrwrite
        FileWriter writer = new FileWriter(filePath, false);
        BufferedWriter bw = new BufferedWriter(writer);

        for(int i=0; i<nList.getLength(); i++){
            Element elem = (Element)nList.item(i);

            System.out.println("자료 : "+getTagValue("cn", elem));
            System.out.println("논문 : "+getTagValue("se", elem));

            String data = getTagValue("cn", elem)+","+
            getTagValue("se", elem);
            bw.write(data); // 한 줄을 쓰고
            bw.newLine(); // 다음 줄로 넘어간다.

            System.out.println();

        }
        bw.close();
        writer.close();
        

        File file = new File(filePath); // 원본파일
        File newFile = new File("/home/Korea/Kor_scs/"+fileName); // 복사된 파일 (flume spooldir 경로)
        FileInputStream in = new FileInputStream(file); // 원본파일을 입력스트림에 배치
        FileOutputStream out = new FileOutputStream(newFile); // 복사될 타겟을 출력스트림에 배치
        // 파일의 내용을 1바이트씩 읽어서, EOF(End Of File : -1)에 도달할 때 까지 반복한다.
        int filebyte = 0;
        while((filebyte = in.read()) != -1) {
            out.write(filebyte);
        }
        out.close();
        in.close();
        
        
    }
    public static String getTagValue(String tag, Element elem) {
        NodeList nlList = null;
        Node nValue = null;
        try {
            nlList = elem.getElementsByTagName(tag).item(0).getChildNodes();
            nValue = (Node) nlList.item(0);
        }
        catch(NullPointerException ne) {
            return "0";
        }
        if(nValue == null) return "0";
        return nValue.getNodeValue();
    }
}
