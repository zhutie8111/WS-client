package cn.com.tcsl.ws.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tony zhu on 2018/11/12.
 */
public class LogUtils {

    public static String LOG_PROFILE_NAME = "WSClient";

    private static Logger logger = LoggerFactory.getLogger(LOG_PROFILE_NAME);

    private static boolean console_print_flag = true;

    public static void console_print(String content){
        if (console_print_flag){
            System.out.println(content);
        }else{
            logger.info(content);
        }

    }
    
    /**
     * The interface to hide the console logs.
     * @param bool
     */
    public static void setConsolePringFlag(boolean bool){
    	console_print_flag = bool;
    }

}
