package dwayne.shim.geogigani.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Temp {

    public static void main(String[] args) throws Exception {
        File file = new File("D:/temp.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // <tr th:if="${destination_detail_info.addr1 != null}">
                //                        <td class="align-middle"><small class="fa fa-phone"></small></td>
                //                        <td class="align-middle"><small>주소</small></td>
                //                        <td class="align-middle"><small th:text="${destination_detail_info.addr1}"></small></td>
                //                      </tr>
                String[] infos = line.split("\\t");
                String kor = infos[0].trim();
                String icon = infos[1]. replaceAll(String.valueOf((char)160), "").trim();
                String lower = infos[2].replaceAll("_", "").toLowerCase();

                //System.out.println("TravelDataIndexField." + upper + ".label(),");
                //removeHtmlTags(docMap, TravelDataIndexField.OVERVIEW.label());
                System.out.println("<tr th:if=\"${destination_detail_info." + lower + " != null && destination_detail_info." + lower + " != ''}\">");
                System.out.println("\t<td class=\"align-middle\"><small class=\"fa " + icon + "\"></small> <small>" + kor + "</small></td>");
                System.out.println("\t<td class=\"align-middle\"><small th:text=\"${destination_detail_info." + lower + "}\"></small></td>");
                System.out.println("</tr>");
                //System.out.println("removeHtmlTags(docMap, TravelDataIndexField." + upper + ".label());");
                //System.out.println("<li class=\"fa " + icon + "\" th:if=\"${destination_detail_info." + lower + " != null}\" th:text=\"' (" + kor + ") : ' + ${destination_detail_info." + lower + "}\"></li>");
                //System.out.println("<br th:if=\"${destination_detail_info." + lower + " != null}\"/>");
            }
        }
    }
}
