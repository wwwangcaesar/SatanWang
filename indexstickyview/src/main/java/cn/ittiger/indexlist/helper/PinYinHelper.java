package cn.ittiger.indexlist.helper;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinMapDict;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author: laohu on 2016/12/17
 * @site: http://ittiger.cn
 */
public class PinYinHelper {

    private static final String PATTERN_LETTER = "^[a-zA-Z]+$";

    /**
     * 将中文转换为pinyin
     */
    public static String getPingYin(String inputString) {

        char[] input = inputString.trim().toCharArray();
        String output = "";
        Pinyin.init(Pinyin.newConfig()
                .with(new PinyinMapDict() {
                    @Override
                    public Map<String, String[]> mapping() {
                        HashMap<String, String[]> map = new HashMap<String, String[]>();
                        map.put("哦", new String[]{"O"});
                        return map;
                    }
                }));
        for (int i = 0; i < input.length; i++) {
            output += Pinyin.toPinyin(String.valueOf(input[i]), "");
        }
        return output.toLowerCase();
    }

    /**
     * 是否为字母
     *
     * @param inputString
     * @return
     */
    public static boolean isLetter(String inputString) {

        return Pattern.matches(PATTERN_LETTER, inputString);
    }
}
