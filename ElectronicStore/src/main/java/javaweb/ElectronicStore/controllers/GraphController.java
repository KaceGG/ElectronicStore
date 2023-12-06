package javaweb.ElectronicStore.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GraphController {
	
	@GetMapping("/bieudo")
    public  String bieudo(){
        return "bieudo";
    }

    @GetMapping("/displayBarGraph")
    public  String barGraph(Model model){
        Map<String,Integer> surveyMap=new LinkedHashMap<>();
        surveyMap.put("C#",80);
        surveyMap.put("Java",40);
        surveyMap.put("Python",20);
        surveyMap.put("C",10);
        model.addAttribute("surveyMap",surveyMap);
        return "barGraph";
    }

    @GetMapping("/displayPieGraph")
    public  String pieChart(Model model){
        model.addAttribute("pass",60);
        model.addAttribute("fail",40);
        return  "pieChart";

    }

    @GetMapping("/displayLineGraph")
    public  String lineChart(){
        return "lineGraph";
    }
}
