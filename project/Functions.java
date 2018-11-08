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
    //�����ļ�ѹ��/*�Ѵ�����ļ�*/
    public static void Compress(File inputFile)throws Exception{
        String isZIP=inputFile.getName().substring(inputFile.getName().length()-4,inputFile.getName().length());
        if (isZIP.equals(".zip")){
            System.out.println("�޷�ѹ����ѹ���ļ�");
        }else {
            int charKinds, r, temp, isEmpty, isFolder = 0;       /*ͳ���ܹ��ж�����ASCII��*//*���ڴ���ASCII����м�洢ֵ*//*�Ƿ�Ϊ��*//*�Ƿ�Ϊ�ļ���*/
            int[] times = new int[256];                    /*����ASCII��Ĵ���*/
            String code = "";
            String s = "";
            ArrayList<HuffmanNode> nodes = new ArrayList<>();
            //���������
            File outFile = new File(inputFile.getCanonicalPath() + ".zip");
            String name = inputFile.getName();/*����ļ�����*/
            FileOutputStream outputStream = new FileOutputStream(outFile);
            BufferedOutputStream fos = new BufferedOutputStream(outputStream);
            if (inputFile.isDirectory()) {
                isFolder = 0;/*0Ϊ�ļ��У�1Ϊ�ļ�*/
                fos.write(name.getBytes());
                fos.write(60);
                fos.write(isFolder);
                fos.close();
            } else {
                isFolder = 1;
                String size = String.valueOf(inputFile.length());
                isEmpty = size.length();
                /*�����ļ�������*/
                FileInputStream inputStream = new FileInputStream(inputFile);
                BufferedInputStream fis = new BufferedInputStream(inputStream);
                while ((r = fis.read()) != -1) {
                    times[r]++;
                }/*д������*/
                fis.close();
                for (int i = 0; i < times.length; i++) {
                    if (times[i] != 0) {
                        nodes.add(new HuffmanNode(times[i], i));
                    }
                }//�����ڵ�
                charKinds = nodes.size();
                HuffmanNode[] nodes1 = new HuffmanNode[charKinds];
                Collections.sort(nodes);/*����*/
                HashMap<Integer, String> HuffmanCode = new HashMap<>();
                CreateHuffmanTree.createTree(nodes, nodes1);/*���ɹ�������*/
                if (nodes.get(0).getWeight() == 0) isEmpty = 0;
                else CreateHuffmanTree.HuffmanCode(nodes1, charKinds, HuffmanCode);/*���ɹ���������*/
                fos.write(name.getBytes());      //ѹ���ļ���
                fos.write(60);                //ѹ���ļ�����ֹ��
                fos.write(isFolder);             //ѹ���Ƿ�Ϊ�ļ���
                if (isEmpty == 0) {
                    fos.write(isEmpty);          //ѹ���Ƿ�Ϊ��
                } else {
                    fos.write(isEmpty);           //ѹ��Ҫ��ȥ�ļ���С�ĳ���
                    fos.write(size.getBytes());   //ѹ���ļ���С

                    if (charKinds == 256) {      /*��ǰ����ASCII�����ж�ѹ���˶����ַ�*/
                        fos.write(charKinds - 1);
                        fos.write(1);
                    } else {
                        fos.write(charKinds);
                        fos.write(0);
                    }
                    /*ѹ����������Ľڵ��ASCIIֵ Ȩ�ش�С����*/
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
                    fos.write(temp);//���ļ�����ת����HuffmanCode����Ȼ��ѹ�������ļ�
                         }
                }
                fos.close();

            }
            System.out.println("ѹ�����");
        }
    }
    //�����ļ���ѹ/*�Ѵ�����ļ�*/
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
        }//��ȡ�ļ���
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
            System.out.println("��ѹ���");
        }
    }
    public static void Compress2(File inputFile,BufferedOutputStream fos,int Length)throws Exception{
        int charKinds, r, temp, isEmpty, isFolder = 0;       /*ͳ���ܹ��ж�����ASCII��*//*���ڴ���ASCII����м�洢ֵ*//*�Ƿ�Ϊ��*//*�Ƿ�Ϊ�ļ���*/
        int[] times = new int[256];                         /*����ASCII��Ĵ���*/
        String code = "";
        String s = "";
        ArrayList<HuffmanNode> nodes = new ArrayList<>();
        //���������
        String name = inputFile.getCanonicalPath();/*����ļ�����*/
        name = name.substring(Length);
        System.out.println(name);
        if (inputFile.isDirectory()) {
            isFolder = 0;/*0Ϊ�ļ��У�1Ϊ�ļ�*/
            fos.write(name.getBytes());
            fos.write(60);
            fos.write(isFolder);
        } else {
            isFolder = 1;
            String size = String.valueOf(inputFile.length());
            isEmpty = size.length();
            /*�����ļ�������*/
            FileInputStream inputStream = new FileInputStream(inputFile);
            BufferedInputStream fis = new BufferedInputStream(inputStream);
            while ((r = fis.read()) != -1) {
                times[r]++;
            }/*д������*/
            fis.close();
            for (int i = 0; i < times.length; i++) {
                if (times[i] != 0) {
                    nodes.add(new HuffmanNode(times[i], i));
                }
            }//�����ڵ�
            charKinds = nodes.size();
            HuffmanNode[] nodes1 = new HuffmanNode[charKinds];
            Collections.sort(nodes);/*����*/
            HashMap<Integer, String> HuffmanCode = new HashMap<>();
            CreateHuffmanTree.createTree(nodes, nodes1);/*���ɹ�������*/
            if (nodes.get(0).getWeight() == 0) isEmpty = 0;
            else CreateHuffmanTree.HuffmanCode(nodes1, charKinds, HuffmanCode);/*���ɹ���������*/
            fos.write(name.getBytes());      //ѹ���ļ���
            fos.write(60);                //ѹ���ļ�����ֹ��
            fos.write(isFolder);             //ѹ���Ƿ�Ϊ�ļ���
            if (isEmpty == 0) {
                fos.write(isEmpty);          //ѹ���Ƿ�Ϊ��
            } else {
                fos.write(isEmpty);           //ѹ��Ҫ��ȥ�ļ���С�ĳ���
                fos.write(size.getBytes());   //ѹ���ļ���С
                if (charKinds == 256) {       /*��ǰ����ASCII�����ж�ѹ���˶����ַ�*/
                    fos.write(charKinds - 1);
                    fos.write(1);
                } else {
                    fos.write(charKinds);
                    fos.write(0);
                }
                /*ѹ����������Ľڵ��ASCIIֵ Ȩ�ش�С����*/
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
                }//���ļ�����ת����HuffmanCode����Ȼ��ѹ�������ļ�
            }
        }
        System.out.println("ѹ�����");
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
        }//��ȡ�ļ���
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
        } //System.out.println("��ѹ���");

    }
    /*�����ҵ�ת����� ʵ�ڲ�����orz*/
    public static String changeIntToString(int value) {
        String s="";
        for (int i = 0; i < 8; i++) {
            s=value%2+s;
            value=value/2;
        }/*�ӵ�λ����λ��������*/
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
