package javaweb.ElectronicStore.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javaweb.ElectronicStore.models.order.Order;
import javaweb.ElectronicStore.service.OrderService;

@Controller
@RequestMapping
public class AdminProfitController {
	
	@Autowired
	private OrderService orderService;
	
	private List<Integer> getAvailableYears() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int startYear = currentYear - 10; 

        List<Integer> years = new ArrayList<>();
        for (int year = currentYear; year >= startYear; year--) {
            years.add(year);
        }
        return years;
    }
	
	private List<String> getMonthsList() {
	    String[] monthNames = new java.text.DateFormatSymbols().getMonths();
	    List<String> months = Arrays.asList(monthNames);
	    return months.subList(0, 12); // Lấy chỉ 12 tháng đầu tiên (từ tháng 0 đến tháng 11)
	}
		
	@GetMapping("/admin/profit")
	public String Profit(@RequestParam(name = "year", defaultValue = "0") int selectedYear, Model model, RedirectAttributes redirectAttributes) {
	    if (selectedYear == 0) {
	        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
	    }

	    List<Integer> years = getAvailableYears();
	    model.addAttribute("years", years);
	    
	 // Thêm danh sách tháng vào model
	    List<String> months = getMonthsList();
	    model.addAttribute("months", months);

	    // Kiểm tra xem có thuộc tính "monthlyProfits" trong FlashAttributes không
	    if (redirectAttributes.getFlashAttributes().containsKey("monthlyProfits")) {
	        // Nếu có, lấy giá trị và đặt vào model
	        List<Float> monthlyProfits = (List<Float>) redirectAttributes.getFlashAttributes().get("monthlyProfits");
	        model.addAttribute("monthlyProfits", monthlyProfits);
	    } else {
	        // Nếu không, thực hiện logic tính toán thông thường
	        List<Float> monthlyProfits = calculateOverallMonthlyProfits(selectedYear);
	        model.addAttribute("monthlyProfits", monthlyProfits);
	    }
	    model.addAttribute("selectedYear", selectedYear);

	    return "AdminProfit";
	}

	
	
	@PostMapping("/admin/profit")
	public String showProfitByYear(@RequestParam("year") int selectedYear, RedirectAttributes redirectAttributes) {
	    List<Float> monthlyProfits = calculateOverallMonthlyProfits(selectedYear);
	    redirectAttributes.addFlashAttribute("monthlyProfits", monthlyProfits);	
	    redirectAttributes.addAttribute("year", selectedYear);
	    return "redirect:/admin/profit";
	}


	// Hàm tính tổng chung doanh thu theo tháng của một năm cụ thể
	private List<Float> calculateOverallMonthlyProfits(int selectedYear) {
	    List<Float> overallMonthlyProfits = new ArrayList<>();
	    
	    // Khởi tạo mảng để lưu tổng doanh thu của 12 tháng
	    for (int i = 1; i <= 12; i++) {
	        overallMonthlyProfits.add(0.0f);
	    }
	    
	    // Lấy danh sách các đơn hàng theo năm từ service hoặc repository của bạn
	    List<Order> ordersByYear = orderService.getOrdersByYear(selectedYear);

	    // Tính tổng doanh thu của từng đơn hàng và cập nhật vào overallMonthlyProfits
	    for (Order order : ordersByYear) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(order.getOrderTime());
	        int month = calendar.get(Calendar.MONTH); // Lấy tháng từ orderTime

	        float totalOrderProfit = order.getTotal(); // Tính tổng doanh thu của đơn hàng
	        float currentMonthProfit = overallMonthlyProfits.get(month);
	        overallMonthlyProfits.set(month, currentMonthProfit + totalOrderProfit);
	    }

	    return overallMonthlyProfits;
	}

}
