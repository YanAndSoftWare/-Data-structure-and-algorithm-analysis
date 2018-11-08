package project;/*
@Author
@Date
@TIME
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Functions {
    //单个文件压缩/*已处理空文件*/
    public static void Compress(File inputFile)throws Exception{
        String isZIP=inputFile.getName().substring(inputFile.getName().length()-4,inputFile.getName().length());
        if (isZIP.equals(".zip")){
            System.out.println("无法压缩已压缩文件");
        }else {
            int charKinds, r, temp, isEmpty, isFolder = 0;       /*统计总共有多少种ASCII码*//*用于储存ASCII码的中间存储值*//*是否为空*//*是否为文件夹*/
            int[] times = new int[256];                    /*储存ASCII码的次数*/
            String code = "";
            String s = "";
            ArrayList<HuffmanNode> nodes = new ArrayList<>();
            //创建输出流
            File outFile = new File(inputFile.getCanonicalPath() + ".zip");
            String name = inputFile.getName();/*获得文件名字*/
            FileOutputStream outputStream = new FileOutputStream(outFile);
            BufferedOutputStream fos = new BufferedOutputStream(outputStream);
            if (inputFile.isDirectory()) {
                isFolder = 0;/*0为文件夹，1为文件*/
                fos.write(name.getBytes());
                fos.write(60);
                fos.write(isFolder);
                fos.close();
            } else {
                isFolder = 1;
                String size = String.valueOf(inputFile.length());
                isEmpty = size.length();
                /*构建文件输入流*/
                FileInputStream inputStream = new FileInputStream(inputFile);
                BufferedInputStream fis = new BufferedInputStream(inputStream);
                while ((r = fis.read()) != -1) {
                    times[r]++;
                }/*写入数组*/
                fis.close();
                for (int i = 0; i < times.length; i++) {
                    if (times[i] != 0) {
                        nodes.add(new HuffmanNode(times[i], i));
                    }
                }//创建节点
                charKinds = nodes.size();
                HuffmanNode[] nodes1 = new HuffmanNode[charKinds];
                Collections.sort(nodes);/*排序*/
                HashMap<Integer, String> HuffmanCode = new HashMap<>();
                CreateHuffmanTree.createTree(nodes, nodes1);/*生成哈夫曼树*/
                if (nodes.get(0).getWeight() == 0) isEmpty = 0;
                else CreateHuffmanTree.HuffmanCode(nodes1, charKinds, HuffmanCode);/*生成哈夫曼编码*/
                fos.write(name.getBytes());      //压入文件名
                fos.write(60);                //压入文件名终止码
                fos.write(isFolder);             //压入是否为文件夹
                if (isEmpty == 0) {
                    fos.write(isEmpty);          //压入是否为空
                } else {
                    fos.write(isEmpty);           //压入要读去文件大小的长度
                    fos.write(size.getBytes());   //压入文件大小

                    if (charKinds == 256) {      /*以前两个ASCII编码判断压入了多少字符*/
                        fos.write(charKinds - 1);
                        fos.write(1);
                    } else {
                        fos.write(charKinds);
                        fos.write(0);
                    }
                    /*压入哈夫曼树的节点的ASCII值 权重从小到大；*/
                    for (int i = 0; i < charKinds; i++) {
                        fos.write(nodes1[i].getData());
                        fos.write(HuffmanCode.get(nodes1[i].getData()).length());
                        code += HuffmanCode.get(nodes1[i].getData());
                    }
                    while (code.length() >= 8) {
                        s = code.substring(0, 8);
                        temp = changeStringToInt(s);
                        fos.write(temp);
                        s = "";
                        code = code.substring(8);
                    }
                    int last1 = 8 - code.length();
                    if (last1 != 8) {
                        for (int i = 0; i < last1; i++) {
                            code += "0";
                        }
                        temp = changeStringToInt(code);
                        fos.write(temp);
                    }
                    code = "";
                    FileInputStream inputStream1 = new FileInputStream(inputFile);
                    BufferedInputStream fis1 = new BufferedInputStream(inputStream1);
                    while ((r = fis1.read()) != -1) {
                        code += HuffmanCode.get(r);
                        if (code.length() >= 8) {
                            s = code.substring(0, 8);
                            temp = changeStringToInt(s);
                            fos.write(temp);
                            code = code.substring(8);
                        }
                    }
                    while (code.length() >= 8) {
                        s = code.substring(0, 8);
                        temp = changeStringToInt(s);
                        fos.write(temp);
                        code = code.substring(8);
                    }
                    int last = 8 - code.length();
                    if (last==8){}
                    else {
                    for (int i = 0; i < last; i++) {
                        code += "0";
                    }
                    temp = Functions.changeStringToInt(code);
                    fos.write(temp);//将文件内容转换成HuffmanCode编码然后压入所需文件
                         }
                }
                fos.close();

            }
            System.out.println("压缩完成");
        }
    }
    //单个文件解压/*已处理空文件*/
    public static void Decompress(File outFile,String Parent)throws Exception{
        FileInputStream InputStream = new FileInputStream(outFile);
        BufferedInputStream fis = new BufferedInputStream(InputStream);
        int r,charKind;
        int codeLength=0;
        int isEmpty;
        int isFolder;
        int codeToStringLength=0;
        String outName ="";
        ArrayList<Byte> name = new ArrayList<>();
        while ((r=fis.read())!=60){
           name.add((byte) r);
        }//获取文件名
        byte [] one = new byte[name.size()];
        for (int i = 0 ; i<name.size();i++){
            one[i]=name.get(i);
        }
        outName = new String(one);
        System.out.println(outName);
        if ((r=fis.read())==0){
            File out = new File(Parent+outName);
            out.mkdir();
        }else {
            File out = new File(Parent+outName);
            out.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(out);
            BufferedOutputStream fos = new BufferedOutputStream(outputStream);
            isEmpty = fis.read();
            if (isEmpty == 0) {
                fis.close();
                fos.close();
            } else {
                String Length = "";
                int Length1 = 0;
                int Length2 = 0;
                for (int i = 0;i<isEmpty;i++){
                    r=fis.read();
                    Length+=(char)r;
                }
                Length1 = Integer.parseInt(Length);
                charKind = fis.read();
                charKind += fis.read();
                int[] Store = new int[charKind];
                int[] pos = new int[charKind];
                HashMap<String, Integer> Huffman = new HashMap<>();

                for (int i = 0; i < charKind; i++) {
                    r = fis.read();
                    Store[i] = r;
                    r = fis.read();
                    pos[i] = r;
                }
                for (int i : pos) {
                    codeLength += i;
                }
                codeToStringLength = codeLength / 8;
                String code = "";
                if (codeLength % 8 != 0) codeToStringLength += 1;
                for (int i = 0; i < codeToStringLength; i++) {
                    r = fis.read();
                    code += changeIntToString(r);
                }
                for (int i = 0; i < charKind; i++) {
                    Huffman.put(code.substring(0, pos[i]), Store[i]);
                    code = code.substring(pos[i]);
                }
                code = "";
                while (Length1!=Length2) {
                    r=fis.read();
                    code += changeIntToString(r);
                    for (int i = 0; i < code.length(); i++) {
                        String codeContent = code.substring(0, i + 1);
                        if (Huffman.get(codeContent) != null) {
                            code = code.substring(i + 1);
                            fos.write(Huffman.get(codeContent));
                            i = -1;
                            Length2+=1;
                            if (Length2==Length1){
                                break;
                            }
                        }
                    }
                }
                fis.close();
                fos.close();
            }
            System.out.println("解压完成");
        }
    }
    public static void Compress2(File inputFile,BufferedOutputStream fos,int Length)throws Exception{
        int charKinds, r, temp, isEmpty, isFolder = 0;       /*统计总共有多少种ASCII码*//*用于储存ASCII码的中间存储值*//*是否为空*//*是否为文件夹*/
        int[] times = new int[256];                         /*储存ASCII码的次数*/
        String code = "";
        String s = "";
        ArrayList<HuffmanNode> nodes = new ArrayList<>();
        //创建输出流
        String name = inputFile.getCanonicalPath();/*获得文件名字*/
        name = name.substring(Length);
        System.out.println(name);
        if (inputFile.isDirectory()) {
            isFolder = 0;/*0为文件夹，1为文件*/
            fos.write(name.getBytes());
            fos.write(60);
            fos.write(isFolder);
        } else {
            isFolder = 1;
            String size = String.valueOf(inputFile.length());
            isEmpty = size.length();
            /*构建文件输入流*/
            FileInputStream inputStream = new FileInputStream(inputFile);
            BufferedInputStream fis = new BufferedInputStream(inputStream);
            while ((r = fis.read()) != -1) {
                times[r]++;
            }/*写入数组*/
            fis.close();
            for (int i = 0; i < times.length; i++) {
                if (times[i] != 0) {
                    nodes.add(new HuffmanNode(times[i], i));
                }
            }//创建节点
            charKinds = nodes.size();
            HuffmanNode[] nodes1 = new HuffmanNode[charKinds];
            Collections.sort(nodes);/*排序*/
            HashMap<Integer, String> HuffmanCode = new HashMap<>();
            CreateHuffmanTree.createTree(nodes, nodes1);/*生成哈夫曼树*/
            if (nodes.get(0).getWeight() == 0) isEmpty = 0;
            else CreateHuffmanTree.HuffmanCode(nodes1, charKinds, HuffmanCode);/*生成哈夫曼编码*/
            fos.write(name.getBytes());      //压入文件名
            fos.write(60);                //压入文件名终止码
            fos.write(isFolder);             //压入是否为文件夹
            if (isEmpty == 0) {
                fos.write(isEmpty);          //压入是否为空
            } else {
                fos.write(isEmpty);           //压入要读去文件大小的长度
                fos.write(size.getBytes());   //压入文件大小
                if (charKinds == 256) {       /*以前两个ASCII编码判断压入了多少字符*/
                    fos.write(charKinds - 1);
                    fos.write(1);
                } else {
                    fos.write(charKinds);
                    fos.write(0);
                }
                /*压入哈夫曼树的节点的ASCII值 权重从小到大；*/
                for (int i = 0; i < charKinds; i++) {
                    fos.write(nodes1[i].getData());
                    fos.write(HuffmanCode.get(nodes1[i].getData()).length());
                    code += HuffmanCode.get(nodes1[i].getData());
                }
                while (code.length() >= 8) {
                    s = code.substring(0, 8);
                    temp = changeStringToInt(s);
                    fos.write(temp);
                    s = "";
                    code = code.substring(8);
                }
                int last1 = 8 - code.length();
                if (last1 != 8) {
                    for (int i = 0; i < last1; i++) {
                        code += "0";
                    }
                    temp = changeStringToInt(code);
                    fos.write(temp);
                }
                code = "";
                FileInputStream inputStream1 = new FileInputStream(inputFile);
                BufferedInputStream fis1 = new BufferedInputStream(inputStream1);
                while ((r = fis1.read()) != -1) {
                    code += HuffmanCode.get(r);
                    if (code.length() >= 8) {
                        s = code.substring(0, 8);
                        temp = changeStringToInt(s);
                        fos.write(temp);
                        code = code.substring(8);
                    }
                }
                while (code.length() >= 8) {
                    s = code.substring(0, 8);
                    temp = changeStringToInt(s);
                    fos.write(temp);
                    code = code.substring(8);
                }
                int last = 8 - code.length();
                if (last==8){}
                else{
                for (int i = 0; i < last; i++) {
                    code += "0";
                }
                temp = Functions.changeStringToInt(code);
                fos.write(temp);
                }//将文件内容转换成HuffmanCode编码然后压入所需文件
            }
        }
        System.out.println("压缩完成");
    }
    public static void Decompress2(BufferedInputStream fis,String Parent)throws Exception{
        int r,charKind,length=0;
        int codeLength=0;
        int isEmpty;
        int isFolder;
        int codeToStringLength=0;
        ArrayList<Byte> Name =new ArrayList<>();
        String outName ="";
        while ((r=fis.read())!=60){
            Name.add((byte)r);
            length++;
        }//获取文件名
        byte [] name = new byte[length];
        for (int i =0;i<length;i++){
            name[i]=Name.get(i);
        }
        outName =new String(name);
        File out = new File(Parent+outName);
        if ((r=fis.read())==0){
            out.mkdir();
        }else {
            System.out.println(Parent+outName);
            FileOutputStream outputStream = new FileOutputStream(out);
            BufferedOutputStream fos = new BufferedOutputStream(outputStream);
            isEmpty = fis.read();
            if (isEmpty == 0) {
                fos.close();
            } else {
                String Length = "";
                int Length1 = 0;
                int Length2 = 0;
                for (int i = 0;i<isEmpty;i++){
                    r=fis.read();
                    Length+=(char)r;
                }
                Length1 = Integer.parseInt(Length);
                charKind = fis.read();
                charKind += fis.read();
                int[] Store = new int[charKind];
                int[] pos = new int[charKind];
                HashMap<String, Integer> Huffman = new HashMap<>();

                for (int i = 0; i < charKind; i++) {
                    r = fis.read();
                    Store[i] = r;
                    r = fis.read();
                    pos[i] = r;
                }
                for (int i : pos) {
                    codeLength += i;
                }
                codeToStringLength = codeLength / 8;
                String code = "";
                if (codeLength % 8 != 0) codeToStringLength += 1;
                for (int i = 0; i < codeToStringLength; i++) {
                    r = fis.read();
                    code += changeIntToString(r);
                }
                for (int i = 0; i < charKind; i++) {
                    Huffman.put(code.substring(0, pos[i]), Store[i]);
                    code = code.substring(pos[i]);
                }
                code = "";
                while (Length1!=Length2) {
                    r=fis.read();
                    code += changeIntToString(r);
                    for (int i = 0; i < code.length(); i++) {
                        String codeContent = code.substring(0, i + 1);
                        if (Huffman.get(codeContent) != null) {
                            code = code.substring(i + 1);
                            fos.write(Huffman.get(codeContent));
                            i=-1;
                            Length2+=1;
                            if (Length2==Length1){
                                break;

                            }
                        }
                    }
                }
                fos.close();
            }
        } //System.out.println("解压完成");

    }
    /*网上找的转码代码 实在不想了orz*/
    public static String changeIntToString(int value) {
        String s="";
        for (int i = 0; i < 8; i++) {
            s=value%2+s;
            value=value/2;
        }/*从低位到高位进行排序*/
        return s;
    }

    public static int changeStringToInt(String s){
        int v1=(s.charAt(0)-48)*128;
        int v2=(s.charAt(1)-48)*64;
        int v3=(s.charAt(2)-48)*32;
        int v4=(s.charAt(3)-48)*16;
        int v5=(s.charAt(4)-48)*8;
        int v6=(s.charAt(5)-48)*4;
        int v7=(s.charAt(6)-48)*2;
        int v8=(s.charAt(7)-48);
        return v1+v2+v3+v4+v5+v6+v7+v8;
    }
}
