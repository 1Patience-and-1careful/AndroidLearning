package com.hanlyjiang.greendaogen.utils;

import java.util.Locale;

/**
 * @author hanlyjiang on 2017/8/24-15:35.
 * @version 1.0
 */

public class DaoUtils {

    public static String createComment(String content) {
        return String.format(Locale.ENGLISH,"/** %s **/",content);
    }
}
