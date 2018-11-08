package project;/*
@Author
@Date
@TIME
*/

import java.util.ArrayList;

public class HuffmanNode implements Comparable{
    private HuffmanNode LChild,RChild,Parent;
    private int weight;
    private int index;
    private int data;
    public HuffmanNode(int weight,int data){
        this.weight=weight;
        this.data=data;
    }

    public void setRChild(HuffmanNode RChild) {
        this.RChild = RChild;
        RChild.setIndex(1);
    }

    public void setLChild(HuffmanNode LChild) {
        this.LChild = LChild;
        LChild.setIndex(0);
    }

    public HuffmanNode getLChild() {
        return LChild;
    }

    public HuffmanNode getRChild() {
        return RChild;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public HuffmanNode getParent() {
        return Parent;
    }

    public void setParent(HuffmanNode parent) {
        this.Parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Object o) {
        HuffmanNode node = (HuffmanNode) o;
        if (this.weight>node.getWeight()){
            return -1;//表明该节点的权重比被比较的节点的小;
        }
        else if (this.weight==node.getWeight())return 0;
        else return 1;//表明该节点的权重比被比较的节点的大
    }
}
