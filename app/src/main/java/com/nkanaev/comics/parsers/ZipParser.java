package com.nkanaev.comics.parsers;

import com.nkanaev.comics.managers.NaturalOrderComparator;
import com.nkanaev.comics.managers.Utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;


public class ZipParser implements Parser {
    private ZipFile mZipFile;
    private ArrayList<ZipArchiveEntry> mEntries;

    @Override
    public void parse(File file) throws IOException {
        mZipFile = new ZipFile(file.getAbsolutePath(), getCharset(file));
        mEntries = new ArrayList<>();

        Enumeration<? extends ZipArchiveEntry> e = mZipFile.getEntries();
        while (e.hasMoreElements()) {
            ZipArchiveEntry ze = e.nextElement();
            if (!ze.isDirectory() && Utils.isImage(ze.getName())) {
                mEntries.add(ze);
            }
        }

        Collections.sort(mEntries, new NaturalOrderComparator() {
            @Override
            public String stringValue(Object o) {
                return ((ZipArchiveEntry) o).getName();
            }
        });
    }

    public String getCharset(File file) {//TODO 侦测效率低下
//        return FileCharsetDetector.getFileEncode(file);
        return "GBK";
    }


    @Override
    public int numPages() {
        return mEntries.size();
    }


    @Override
    public InputStream getPage(int num) throws IOException {
        return mZipFile.getInputStream(mEntries.get(num));
    }

    @Override
    public String getType() {
        return "zip";
    }

    @Override
    public void destroy() throws IOException {
        mZipFile.close();
    }
}
