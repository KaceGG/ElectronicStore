package javaweb.ElectronicStore.dto;

import java.util.List;

import javaweb.ElectronicStore.models.order.PaymentMethod;

public class OrderDto {
	public static PaymentMethod paymentType;
	public static Long userId;
	public static float subtotal;
	public static List<Long> productIds;
	public static List<Integer> quantities;
	public static String city;
	public static String district;
	public static String ward;
	public static String street;
	
	public static void clear() {
		paymentType = null;
		userId = null;
		subtotal = 0;
		productIds = null;
		quantities = null;
		city = null;
		district = null;
		ward = null;
		street = null;
	}
}
