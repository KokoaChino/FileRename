package org.example;

import org.example.file.FileDelete;
import org.example.file.FileRename;

import java.io.File;


public class Main {
    public static void main(String[] args) {
//        FileReorder.run(new File("D:\\资源\\图片\\点兔自截图\\长图\\角色"));
        File[] files = new File("D:\\software\\Release 2.7.3\\.minecraft\\versions\\更完美的MC  1.21.1\\shaderpacks").listFiles();
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(".zip")) System.out.println(file.getName());
        }
    }
}