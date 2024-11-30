package com.group7.blog.controllers;

import com.group7.blog.dto.Category.request.CategoryCreateRequest;
import com.group7.blog.dto.Category.response.CategoryDetailResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.models.Category;
import com.group7.blog.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "This endpoint allows you to create a new category",
            tags = {"Categories"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
            }
    )
    
    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody CategoryCreateRequest request) {
        return ApiResponse.<Category>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @Operation(
            summary = "Get all categories",
            description = "This endpoint returns a list of all categories",
            tags = {"Categories"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Categories fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public ApiResponse<List<CategoryDetailResponse>> getCategories() {
        return ApiResponse.<List<CategoryDetailResponse>>builder()
                .result(categoryService.getCategories())
                .build();
    }

    @Operation(
            summary = "Get category details by ID",
            description = "This endpoint retrieves details of a specific category by ID",
            tags = {"Categories"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDetailResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
            }
    )
    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryDetailResponse> getCategory(@PathVariable("categoryId") UUID categoryId) {
        return ApiResponse.<CategoryDetailResponse>builder()
                .result(categoryService.getCategory(categoryId))
                .build();
    }
}
