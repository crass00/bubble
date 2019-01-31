package com.nkanaev.comics.parsers;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;


/**
 * Title :    
 * Author : zhongchun.long@ubtrobot.com 
 * Date : 2019/1/31
 * Project : bubble
 */
/**
 * 1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来,
 *    ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector.
 * 2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则.
 * 3、cpDetector是基于统计学原理的,不保证完全正确.
 */
public class FileCharsetDetector {

    private static final String TAG = "FileCharsetDetector";

    /**
     * 利用第三方开源包cpdetector获取文件编码格式.
     *
     * @param is
     *            InputStream 输入流
     * @return
     */
    public static String getFileEncode(InputStream is) {
        //    begin     此段为zip格式文件的处理关键
        BufferedInputStream bis = null;
        if (is instanceof BufferedInputStream) {
            bis = (BufferedInputStream) is;
        } else {
            bis = new BufferedInputStream(is);
        }
        //   end

        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

        detector.add(new ParsingDetector(false));
        detector.add(UnicodeDetector.getInstance());
        detector.add(JChardetFacade.getInstance());// 内部引用了 chardet.jar的类
        detector.add(ASCIIDetector.getInstance());

        Charset charset = null;
        try {
            charset = detector.detectCodepage(bis, Integer.MAX_VALUE);// zip 判断的关键代码
        } catch (Exception e) {
            Log.e(TAG,e.getMessage(), e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        // 默认为GBK
        String charsetName = "GBK";
        if (charset != null) {
            if (charset.name().equals("US-ASCII")) {
                charsetName = "ISO_8859_1";
            } else {
                charsetName = charset.name();
            }
        }
        return charsetName;
    }


    public static String getFileEncode(File  file) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

        detector.add(new ParsingDetector(false));
        detector.add(UnicodeDetector.getInstance());
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());

        Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        // 默认为GBK
        String charsetName = "GBK";
        if (charset != null) {
            if (charset.name().equals("US-ASCII")) {
                charsetName = "ISO_8859_1";
            } else {
                charsetName = charset.name();
            }
        }
        return charsetName;
    }

}
