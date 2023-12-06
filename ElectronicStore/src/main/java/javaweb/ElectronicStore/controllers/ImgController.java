	package javaweb.ElectronicStore.controllers;
	
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	
	import org.springframework.core.io.ByteArrayResource;
	import org.springframework.http.MediaType;
	import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.ResponseBody;
	
	@Controller
	public class ImgController {
		private static final String IMAGE_DIRECTORY = "src/main/resources/static/assets/img/";
		
		@GetMapping("/getbrandimage/{brand}/{photo}")
		@ResponseBody
		public ResponseEntity<ByteArrayResource> getBrandImage(
		        @PathVariable("brand") String brand,
		        @PathVariable("photo") String photo) {
		    return getImageFromDirectory(brand, photo);
		}
		
		@GetMapping("/getcategoryimage/{category}/{photo}")
		@ResponseBody
		public ResponseEntity<ByteArrayResource> getCategoryImage(
		        @PathVariable("category") String category,
		        @PathVariable("photo") String photo) {
		    return getImageFromDirectory(category, photo);
		}
		
		@GetMapping("/getproductimage/{products}/{photo}")
		@ResponseBody
		public ResponseEntity<ByteArrayResource> getProductImage(
		        @PathVariable("products") String products,
		        @PathVariable("photo") String photo) {
		    return getImageFromDirectory(products, photo);
		}
		
		
		private ResponseEntity<ByteArrayResource> getImageFromDirectory(String directory, String photo) {
		    if (photo != null && !photo.equals("")) {
		        try {
		            Path filename = Paths.get(IMAGE_DIRECTORY, directory, photo);
		            byte[] buffer = Files.readAllBytes(filename);
		            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
		            return ResponseEntity.ok()
		                    .contentLength(buffer.length)
		                    .contentType(MediaType.parseMediaType("image/png"))
		                    .body(byteArrayResource);
		        } catch (Exception e) {
		            // In thông điệp lỗi để kiểm tra
		            e.printStackTrace();
		        }
		    }
		    return ResponseEntity.badRequest().build();
		}
	}
