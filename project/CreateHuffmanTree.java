package project;/*
@Author
@Date
@TIME
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateHuffmanTree {
    public static void createTree(ArrayList<HuffmanNode> nodes , HuffmanNode [] nodes1){
        int i = 0;
        if(nodes.size()==0){
            nodes.add(new HuffmanNode(0,'空'));
        }
        else while (nodes.size()!=1){
            HuffmanNode one =nodes.remove(nodes.size()-1);//取最后一个
            HuffmanNode two =nodes.remove(nodes.size()-1);//取倒数第二个
            HuffmanNode parent = new HuffmanNode(one.getWeight()+two.getWeight(),-1);
            one.setParent(parent);
            two.setParent(parent);
            if (one.getData()>=0){
                nodes1[i]=one;
                i+=1;
            }
            if (two.getData()>=0){
                nodes1[i]=two;
                i+=1;
            }
            parent.setLChild(one);
            parent.setRChild(two);
            nodes.add(parent);
            Collections.sort(nodes);
        }
    }
    public static void HuffmanCode(HuffmanNode [] nodes, int charKinds, HashMap<Integer,String> hashMap){
        String s = "";
        for(int i = 0;i<charKinds;i++){
            HuffmanNode one = nodes[i];
            while (one.getParent()!=null){
                s=one.getIndex()+s;
                one=one.getParent();
            }
            hashMap.put(nodes[i].getData(),s);
            s="";
        }
    }
}
