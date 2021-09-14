package com.greenart.utils;

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


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Component
public class DaeguAPIcintroller {
    @Scheduled(cron="0 */1 * * * *")
    public void getDaeguinfoXMl() throws Exception {

        StringBuilder builder = new StringBuilder("http://car.daegu.go.kr/openapi-data/service/rest/data/linkspeed"); //요청 url
        builder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") +
            "=6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D"); //ServiceKey 입력
        
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(builder.toString()); // 공공데이터 요청 url
            // System.out.println(urlBuilder.toString());

            doc.getDocumentElement().normalize(); //java에서 파싱가능 하도록 format전환
            System.out.println(doc.getDocumentElement().getNodeName()); //전체문서 갔고오기
            NodeList nList = doc.getElementsByTagName("item"); //1덩어리 = 노드
            // System.out.println(nList.getLength()); // 요청노드 개수
            
            String fileName = "kor_scs"+DateUtils.makeTodayString()+".txt";
            String filePath = "/home/Korea/Kor/"+fileName;
            // ture - append/ false - overrwrite
            FileWriter writer = new FileWriter(filePath, false);
            BufferedWriter bw = new BufferedWriter(writer);
    
            for(int i=0; i<nList.getLength(); i++){
                Element elem = (Element)nList.item(i);
    
                System.out.println(" : "+getTagValue("", elem));
                System.out.println(" : "+getTagValue("", elem));
    
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