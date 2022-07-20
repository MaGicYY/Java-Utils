package com.zxy.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaoxinyang
 * @date 2022/6/30
 */
@Slf4j
public class RegexUtil {

    public static String findFirstMatchValue(Pattern pattern, String str, int group) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            if (matcher.groupCount() < group) {
                log.error("invalid groupCount,pattern:[{}],str:[{}],group:[{}]", pattern.pattern(), str, group);
                throw new IllegalArgumentException();
            }
            return matcher.group(group);
        }

        return null;
    }

    public static String replacePlaceHolder(Pattern pattern, int group, String str, Map<String, String> params) {
        Matcher matcher = pattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (matcher.find()) {
            if (matcher.groupCount() < group) {
                log.error("invalid groupCount,pattern:[{}],str:[{}],group:[{}]", pattern.pattern(), str, group);
                throw new IllegalArgumentException();
            }
            String key = matcher.group(group);
            int start = matcher.start();
            sb.append(str, index, start);
            sb.append(params.get(key));
            index = matcher.end();
        }

        if (index < str.length()) {
            sb.append(str, index, str.length());
        }
        return sb.toString();
    }


}
