package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<String>> deliveryPartnerOrderHashMap = new HashMap<>();

    HashMap<String, String> Assigned = new HashMap<>();

    public void addOrder(Order order){
        String id = order.getId();
        orderHashMap.put(id,order);
    }

    public void addPartner(String id){
        DeliveryPartner deliveryPartner = new DeliveryPartner(id);
        deliveryPartnerHashMap.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        if(orderHashMap.containsKey(orderId) && deliveryPartnerHashMap.containsKey(partnerId))
        {
            Assigned.put(orderId,partnerId);

            List<String> currentOrder = new ArrayList<>();
            if(deliveryPartnerOrderHashMap.containsKey(partnerId))
            {
                currentOrder = deliveryPartnerOrderHashMap.get(partnerId);
            }
            currentOrder.add(orderId);
            deliveryPartnerOrderHashMap.put(partnerId,currentOrder);

            DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
        }
    }

    public Order getOrderById(String id){
        return orderHashMap.get(id);
    }

    public DeliveryPartner getPartnerById(String id){
        return deliveryPartnerHashMap.get(id);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return deliveryPartnerOrderHashMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return deliveryPartnerOrderHashMap.get(partnerId);
    }

    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        for(String order : orderHashMap.keySet())
        {
            orders.add(order);
        }
        return orders;
    }

    public int getCountOfUnassignedOrders(){
        return orderHashMap.size() - Assigned.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId){
        int countOfOrders = 0;
        List<String> orders = deliveryPartnerOrderHashMap.get(partnerId);
        for (String order : orders) {
            int deliveryTime = orderHashMap.get(order).getDeliveryTime();
            if (deliveryTime > time) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int maxTime = 0;
        List<String> orders = deliveryPartnerOrderHashMap.get(partnerId);

        for(String order : orders)
        {
            int currentTime = orderHashMap.get(order).getDeliveryTime();
            maxTime = Math.max(maxTime,currentTime);
        }
        return maxTime;
    }

    public void deletePartnerById(String partnerId){
        deliveryPartnerHashMap.remove(partnerId);
        List<String> list = deliveryPartnerOrderHashMap.get(partnerId);
        deliveryPartnerOrderHashMap.remove(partnerId);

        for(String order : list)
        {
            Assigned.remove(order);
        }
    }

    public void deleteOrderById(String orderId){
        orderHashMap.remove(orderId);
        String partnerId = Assigned.get(orderId);
        Assigned.remove(orderId);

        deliveryPartnerOrderHashMap.get(partnerId).remove(orderId);

        deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(deliveryPartnerOrderHashMap.get(partnerId).size());

    }
}