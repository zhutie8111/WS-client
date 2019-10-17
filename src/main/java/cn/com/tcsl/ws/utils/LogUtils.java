package cn.com.tcsl.ws.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Created by Tony zhu on 2018/11/12.
 */
public class LogUtils {

    private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String LOG_PROFILE_NAME = "WSClient";

    private static Logger logger = LoggerFactory.getLogger(LOG_PROFILE_NAME);

    private static boolean console_print_flag = true;

    public static void console_print(String content){

        if (console_print_flag){
            String consoleLog = yyyy_MM_dd_HH_mm_ss.format(System.currentTimeMillis()) +" "+ content;
            System.out.println(consoleLog);
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
