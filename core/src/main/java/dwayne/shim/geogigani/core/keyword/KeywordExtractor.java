package dwayne.shim.geogigani.core.keyword;

import dwayne.shim.korean.analyzer.core.config.MainConf;
import dwayne.shim.korean.analyzer.core.morph.Morph;
import dwayne.shim.korean.analyzer.core.morph.MorphAnalyzer;
import dwayne.shim.korean.analyzer.core.morph.Morphs;
import dwayne.shim.korean.analyzer.core.token.TokenAnalyzer;
import dwayne.shim.korean.analyzer.core.token.Tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeywordExtractor {

    public KeywordExtractor(String configFileLocation) {
        MainConf.customOnlyOnce(configFileLocation);
    }

    public List<String> extract(String line) {
        List<String> keywordList = new ArrayList<>();

        // 1. analyze morpheme
        Tokens tokens = TokenAnalyzer.getInstance().parseToken(line.toCharArray());
        Morphs morphs = MorphAnalyzer.getInstance().parseMorph(tokens);

        // 2. filter accepted keywords ...
        Iterator<Morph> iter = morphs.morphIterator();
        while(iter.hasNext()) {
            Morph morph = iter.next();
            if(morph == null) continue;

            // use only noun and english
            String pos = morph.getPosStr();
            if(!pos.startsWith("nn") && !pos.startsWith("unk") && !pos.startsWith("sn")) continue;

            String keyword = morph.getTextStr().toLowerCase();
            if(keyword.length() <= 1) continue;
            if(hasNumberIn(keyword)) continue;
            keywordList.add(keyword);
        }

        return keywordList;
    }

    private boolean hasNumberIn(String keyword) {
        for(char ch : keyword.toCharArray())
            if(Character.isDigit(ch)) return true;

        return false;
    }

    public static void main(String[] args) throws Exception {
        KeywordExtractor ke = new KeywordExtractor("D:/korean-analyzer/configurations/main.conf");
        List<String> keywords = ke.extract("전주 이화호텔은 전주한옥마을 서편의 전동에 위치한 지하 1층, 지상 3층짜리 건물이다.");
        System.out.println(keywords);
    }
}
