package com.paouke.code.magicbox.container.list;

import java.util.Arrays;

//循环列表
public class LoopList<D> implements Cloneable, java.io.Serializable {

    private static final int DEFAULT_LENGTH = 20;

    private static final int MAX_LENGTH = 100000;

    private volatile int cursor = -1;

    private volatile int head = 0;

    private volatile int length;

    private volatile Object listData[];

    private volatile boolean empty = true;

    public LoopList() {
        listData = new Object[DEFAULT_LENGTH];
        length = DEFAULT_LENGTH;
    }

    public LoopList(int length) {
        if (length > MAX_LENGTH || length <= 0) {
            throw new IllegalArgumentException("Exceed the maximum length of 100000");
        }
        listData = new Object[length];
        this.length = length;
    }

    //获取循环列表长度
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        if (isLoop()) {
            return length;
        }
        return head > cursor ? length - (head - cursor - 1) : (cursor - head + 1);
    }

    //判断循环列表是否为空
    public boolean isEmpty() {
        return empty;
    }

    //获取循环列表的尾
    public D tail() {
        if (empty) {
            return null;
        }
        return data(cursor);
    }

    //获取循环列表的头
    public D head() {
        if (empty) {
            return null;
        }
        return data(head);
    }

    //获取列表中某个元素
    public D get(int i) {
        return data(reallyIndex(i));
    }

    //判断某个元素的位置
    public int indexOf(Object o) {
        if(o == null) {
            for(int i = 0;i < size();i++) {
                if(get(i) == null) {
                    return i;
                }
            }
        } else {
            for(int i = 0;i < size();i++) {
                if(o.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    //判断是否包含某个元素
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    //将循环列表转为数组
    @SuppressWarnings("unchecked")
    public D[] toArray() {
        Object[] empty = {};
        Object[] datas = size() == 0 ? empty : new Object[size()];
        for (int i = 0; i < size(); i++) {
            datas[i] = get(i);
        }
        return (D[]) datas;
    }

    //在循环列表中新增数据
    public synchronized void add(D d) {
        empty = false;
        head = (isLoop() ? (head == length - 1 ? 0 : head + 1) : head);
        cursor = (cursor == length - 1 ? 0 : cursor + 1);
        listData[cursor] = d;
    }

    //设置某个index的值，不能超过size
    public synchronized void set(int i, D data) {
        listData[reallyIndex(i)] = data;
    }

    //从尾部删除一个数据
    public synchronized void remove() {
        if (empty) {
            throw new IndexOutOfBoundsException("try to remove the element in the empty list");
        }
        empty = (head == cursor);
        listData[cursor] = null;
        cursor = (cursor == 0 ? length - 1 : cursor - 1);
    }

    //初始化List
    public synchronized void clear() {
        empty = true;
        for (int i = 0; i < length; i++) {
            listData[i] = null;
        }
        head = 0;
        cursor = -1;
    }

    //重新设置大小
    public synchronized void grow(int newLength) {
        if (newLength > length) {
            Object[] newListData = new Object[newLength];
            for (int i = 0; i < size(); i++) {
                newListData[i] = get(i);
            }
            cursor = cursor == -1 ? -1 : size() - 1;
            head = 0;
            empty = (cursor == -1);
            length = newLength;
            listData = newListData;
        }
    }

    @SuppressWarnings("unchecked")
    private D data(int index) {
        return (D) listData[index];
    }


    private int reallyIndex(int index) {
        if (!isLoop() && index >= size()) {
            throw new IndexOutOfBoundsException("too max index then LoopList size");
        }
        if (index > length - 1) {
            index %= (length - 1);
        }
        if(index < 0) {
            index = length - (Math.abs(index % length));
        }
        return head + index > length - 1 ? index - (length - head) : head + index;
    }

    private boolean isLoop() {
        return !empty && ((cursor != -1 && head - cursor == 1) || cursor - head == length - 1);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public static void main(String[] args) throws Exception {
        LoopList<Integer> loopList = new LoopList<>(5);
        loopList.add(1);
        loopList.add(2);
        loopList.add(3);
        loopList.add(4);
        loopList.add(5);
        System.out.println(loopList.get(1));
        System.out.println(loopList.toString());
        System.out.println(loopList.indexOf(10));
    }

}
