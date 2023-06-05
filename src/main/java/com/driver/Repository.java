package com.driver;

import java.util.*;

public class Repository {
    HashMap<String,DeliveryPartner>deliveryPartnerHashMap=new HashMap<>();
    HashMap<String,Order>orderDb=new HashMap<>();
    HashMap<String, List<String>>orderPairDb=new HashMap<>();
    HashMap<String,String>assignedDb=new HashMap<>();

    public void addOrder(Order order){
        orderDb.put(order.getId(),order);


    }
    public void addPartner(DeliveryPartner deliveryPartner){
        deliveryPartnerHashMap.put(deliveryPartner.getId(), deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId){
        assignedDb.put(partnerId,orderId);
        List<String>list=orderPairDb.getOrDefault(partnerId,new ArrayList<>());
        list.add(orderId);
        assignedDb.put(orderId,partnerId);
        DeliveryPartner deliveryPartner=deliveryPartnerHashMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(list.size());
        return;
    }
    public Order getOrderById(String orderId){
        for(String s:orderDb.keySet()){
            if(s.equals(orderId)){
                return orderDb.get(s);
            }
        }
        return  null;
    }
    public DeliveryPartner getPartnerById(String partnerID){
        if(deliveryPartnerHashMap.containsKey(partnerID)){
            return deliveryPartnerHashMap.get(partnerID);
        }
        return  null;
    }
    public Integer getOrderCountByPartnerId(String partnerId){
        int orderCount=orderPairDb.getOrDefault(partnerId,new ArrayList<>()).size();
        return  orderCount;
    }

   public List<String>getOrdersByPartnerId(String partnerId){
        List<String>orders=orderPairDb.getOrDefault(partnerId,new ArrayList<>());
        return orders;
   }
    public List<String> getAllOrders(){
        List<String>orders=new ArrayList<>();
        for(String s:orderDb.keySet()){
            orders.add(s);
        }
        return  orders;
    }
    public Integer  getCountOfUnassignedOrders(){
        int count=orderDb.size()-assignedDb.size();
        return count;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerID){
        int count=0;
        List<String>list=orderPairDb.get(partnerID);
        int deliveryTime=Integer.parseInt(time.substring(0,2))*Integer.parseInt(time.substring(3));
         for(String s:list){
             Order order=orderDb.get(s);
             if(order.getDeliveryTime()>deliveryTime){
                 count++;
             }
         }
         return  count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time="";
        List<String>list=orderPairDb.get(partnerId);
        int deliveryTime=0;
        for(String s:list){
            Order order=orderDb.get(s);
            deliveryTime=Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour=deliveryTime/60;
        String sHour="";
        if(hour<10){
            sHour="0"+String.valueOf(hour);
        }else{
            sHour=String.valueOf(hour);
        }
        int min=deliveryTime%60;
        String smin="0"+String.valueOf(min);
        if(min<10){
            smin=String.valueOf(min);
        }else{
            smin=String.valueOf(min);
        }
        time=sHour+":"+smin;
        return  time;
    }
    public void deletePartnerById(String partnerID){
        deliveryPartnerHashMap.remove(partnerID);
        List<String>list=orderPairDb.getOrDefault(partnerID,new ArrayList<>());
        ListIterator<String>itr=list.listIterator();
        while(itr.hasNext()){
            String s= itr.next();
            assignedDb.remove(s);
        }
        orderPairDb.remove(partnerID);
    }
    public void deleteOrderById(String orderID){
        orderDb.remove(orderID);
        String partnerID=assignedDb.get(orderID);
        assignedDb.remove(orderID);
        List<String>list=orderPairDb.get(partnerID);
        ListIterator<String>itr= list.listIterator();
        while (itr.hasNext()){
            String s= itr.next();
            if(s.equals(orderID)){
                itr.remove();
            }
        }
        orderPairDb.put(partnerID,list);



    }
}
