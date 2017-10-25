package com.spider.DataHelper;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by zhoumeng on 2017/10/24
 */
public class ChangeName {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        getFileName();
    }

    /*
     * 文件重命名
     */
    public static boolean renameFile(String file, String toFile) {

        File toBeRenamed = new File(file);
        // 检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

            System.out.println("文件不存在: " + file);
            return false;
        }

        File newFile = new File(toFile);

        // 修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            System.out.println("重命名成功.");
            return true;
        } else {
            System.out.println("重命名失败");
            return false;
        }

    }

    /*
     * 文件夹下文件所有文件展示
     */
    public static void getFileName() {
        String path = "/Users/zhoumeng/Desktop/UAV-Images-test/"; // 路径
        String path1 = "/Users/zhoumeng/Desktop/UAV-Images-raw/";
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " 不存在");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                String nameString = fs.getName();
                if (nameString.contains("jpg")) {
                    //部分文件名修改
                    nameString = String.format("%05d", i) + ".jpg";
                    fileChannelCopy(path + fs.getName(), path1 + nameString);
                    System.out.println( i + "/"  + fa.length);

                }
            }
        }
    }

    private static void fileChannelCopy(String srcDirName,String destDirName) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(new File(srcDirName));
            fo = new FileOutputStream( new File(destDirName));
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            /*
             *       public abstract long transferTo(long position, long count,
                                         WritableByteChannel target)throws IOException;
             *          position - 文件中的位置，从此位置开始传输；必须为非负数
             *          count - 要传输的最大字节数；必须为非负数
             *          target - 目标通道
             *          返回：
                        实际已传输的字节数，可能为零
             */
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道中
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

}
