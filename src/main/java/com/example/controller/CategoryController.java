package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;

import com.example.constants.Message;
import com.example.model.Category;
import com.example.model.CategoryProduct;
import com.example.service.CategoryService;
// 課題１１
import com.example.service.CategoryProductService;
import com.example.model.Product;
import com.example.service.ProductService;
import com.example.utils.CheckUtil;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	// 課題１１
	@Autowired
	private CategoryProductService categoryProductService;

	@GetMapping
	public String index(Model model) {
		List<Category> all = categoryService.findAllOrderByDisplayOrder();
		model.addAttribute("listCategory", all);
		return "category/index";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable("id") Long id) {
		if (id != null) {
			Optional<Category> category = categoryService.findOne(id);
			model.addAttribute("category", category.get());
		}
		return "category/show";
	}

	@GetMapping(value = "/new")
	public String create(Model model, @ModelAttribute Category entity) {
		model.addAttribute("category", entity);
		return "category/form";
	}

	@PostMapping
	@SuppressWarnings("unused")
	public String create(@Validated @ModelAttribute Category entity, BindingResult result,
			RedirectAttributes redirectAttributes) {
		Category category = null;
		try {
			// descriptionは2000文字まで
			if (!CheckUtil.checkDescriptionLength(entity.getDescription())) {
				// NG
				redirectAttributes.addFlashAttribute("error", Message.MSG_VALIDATE_ERROR);
				return "redirect:/categories";
			}

			category = categoryService.save(entity);
			redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_INSERT);
			redirectAttributes.addAttribute("q", "create");
			// 課題９
			// return "redirect:/categories";
			return "redirect:/categories/" + category.getId() + "/productRelation";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
			e.printStackTrace();
			return "redirect:/categories";
		}
	}

	@GetMapping("/{id}/edit")
	public String update(Model model, @PathVariable("id") Long id) {
		try {
			if (id != null) {
				Optional<Category> entity = categoryService.findOne(id);
				model.addAttribute("category", entity.get());
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return "category/form";
	}

	@PutMapping
	public String update(@Validated @ModelAttribute Category entity, BindingResult result,
			RedirectAttributes redirectAttributes) {
		Category category = null;
		try {
			// descriptionは2000文字まで
			if (!CheckUtil.checkDescriptionLength(entity.getDescription())) {
				// NG
				redirectAttributes.addFlashAttribute("error", Message.MSG_VALIDATE_ERROR);
				return "redirect:/categories";
			}

			category = categoryService.save(entity);
			redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_UPDATE);
			redirectAttributes.addAttribute("q", "update");

			return "redirect:/categories/" + category.getId() + "/productRelation";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
			e.printStackTrace();
			return "redirect:/categories";
		}
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			if (id != null) {
				Optional<Category> entity = categoryService.findOne(id);
				categoryService.delete(entity.get());
				redirectAttributes.addFlashAttribute("success", Message.MSG_SUCESS_DELETE);
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", Message.MSG_ERROR);
			throw new ServiceException(e.getMessage());
		}
		return "redirect:/categories";
	}

	@GetMapping("/{id}/productRelation")
	public String productRelation(Model model, @PathVariable("id") Long id,
			@RequestParam(name = "q", required = false) String q) {
		if (id != null) {
			List<Product> listProduct = productService.findAll();
			Optional<Category> category = categoryService.findOne(id);
			// 課題１１
			// Retrieve the product IDs associated with the category
			List<CategoryProduct> associatedProductIds = categoryProductService.findByCategoryId(id);

			model.addAttribute("category", category.get());
			model.addAttribute("products", listProduct);
			// 課題１１
			model.addAttribute("associatedProductIds", associatedProductIds);

			if (q != null && (q.equals("create") || q.equals("update"))) {
				model.addAttribute("action", true);
			} else {
				model.addAttribute("action", false);
			}

			System.out.println("中身は、" + associatedProductIds);
			System.out.println("中身は、" + listProduct);
		}
		return "category/productRelation";
	}

	// 課題１１
	@PostMapping("/{categoryId}/updateProductRelation")
	public ResponseEntity<String> updateProductCategoryRelation(
			@PathVariable Long categoryId,
			@RequestBody List<Long> productIds) {

		boolean success = categoryService.deleteInsertCategoryProduct(categoryId, productIds);

		if (success) {
			return new ResponseEntity<>("紐付更新が成功しました", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("紐付更新中にエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}