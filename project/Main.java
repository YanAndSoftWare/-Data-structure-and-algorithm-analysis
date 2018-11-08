package project;/*
@Author
@Date
@TIME
*/


import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)throws Exception {
        Scanner scanner = new Scanner(System.in);
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String order ="";
        String path  ="";
        String isOK  ="";
        while (!order.equals("x")) {
            System.out.println("������ָ�      -����'h'���Ի�ð�����Ϣ");
            order=scanner.next();
            switch (order){
                case "h":System.out.println("0:ѹ�������ļ�;1:ѹ���ļ���;2:��ѹ�����ļ�ѹ���ļ�;3:��ѹ�ļ���ѹ���ļ�;x:�˳��ó���;h:��ȡ������Ϣ;\nps:����ϸ������Ҫѹ�����ѹ�����ݣ��������ִ���");break;
                case "0":{
                    System.out.println("�������ļ��ľ���·��");
                    path=bf.readLine();
                    run(order,path);
                    System.out.println("ѹ�����");
                };break;
                case "1":{
                    System.out.println("�������ļ��ľ���·��");
                    path=bf.readLine();
                    run(order,path);
                    System.out.println("��ѹ���");
                };break;
                case "2":{
                    System.out.println("�������ļ��ľ���·��");
                    path=bf.readLine();
                    run(order,path);
                    System.out.println("ѹ�����");
                };break;
                case "3":{
                    System.out.print("�������ļ��ľ���·��");
                    path=bf.readLine();
                    path=path.substring(1);
                    run(order,path);
                    System.out.println("��ѹ���");
                };break;
                case "x":System.out.println("�����˳������Ժ�");break;
            }
        }
    }
    private static void run(String a,String path)throws Exception{
        double StartTime = System.nanoTime();
        File File = new File(path);
        switch (a){
            case "0":Functions.Compress(File);break;
            case "1":{
                File outFile1 = new File(path+".zip");
                String Parent = File.getName();
                int Length=path.length()-Parent.length();
                System.out.println(Length);
                FileOutputStream outputStream = new FileOutputStream(outFile1);
                BufferedOutputStream fos = new BufferedOutputStream(outputStream);
                TraFolder(path,fos,Length);
                fos.close();
            };break;
            case "2":{
                String parent = File.getParent();
                if (parent.length()==3){
                }else parent+="\\";
                Functions.Decompress(File,parent);
            };break;
            case "3":{
                String parent = File.getParent();
                if (parent.length()==3){
                }else parent=parent+"\\";
                FileInputStream inputStream = new FileInputStream(File);
                BufferedInputStream fis = new BufferedInputStream(inputStream);
                while (fis.available()>1){
                    Functions.Decompress2(fis,parent);
                }
                fis.close();}break;
        }
        double EndTime = System.nanoTime();
        double RunTime = (EndTime - StartTime);
        System.out.println(RunTime/1000000000+"s");

    }
    public static void TraFolder(String path,BufferedOutputStream fos,int Length)throws Exception {
        File file = new File(path);
        Functions.Compress2(file,fos,Length);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        TraFolder(file2.getAbsolutePath(),fos,Length);
                    } else {
                        Functions.Compress2(file2,fos,Length);
                    }
                }
            }
        }
    }
}
