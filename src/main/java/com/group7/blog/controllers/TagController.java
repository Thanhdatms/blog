package com.group7.blog.controllers;

import com.group7.blog.dto.Blog.request.BlogFilter;
import com.group7.blog.dto.Tag.response.TagResponse;
import com.group7.blog.dto.User.reponse.ApiResponse;
import com.group7.blog.dto.Tag.request.TagUpdateRequest;
import com.group7.blog.dto.Tag.request.TagCreateRequest;
import com.group7.blog.services.TagService;
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
@RequestMapping("/tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagController {
    TagService tagService;

    @Operation(
            summary = "Create a new tag",
            description = "This endpoint allows you to create a new tag.",
            tags = {"Tags"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Tag created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping
    public ApiResponse<TagResponse> createTag(@RequestBody TagCreateRequest request) {
        return ApiResponse.<TagResponse>builder()
                .result(tagService.createTag(request))
                .build();
    }

    @Operation(
            summary = "Get all tags",
            description = "This endpoint retrieves all tags.",
            tags = {"Tags"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Tags fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public ApiResponse<List<TagResponse>> getTags() {
        return ApiResponse.<List<TagResponse>>builder()
                .result(tagService.getTags())
                .build();
    }

    @Operation(
            summary = "Get a specific tag",
            description = "This endpoint retrieves details of a specific tag.",
            tags = {"Tags"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Tag fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
            }
    )
    @GetMapping("/{tagId}")
    public ApiResponse<TagResponse> getTag(@PathVariable("tagId") UUID tagId) {
        return ApiResponse.<TagResponse>builder()
                .result(tagService.getTag(tagId))
                .build();
    }

    @Operation(
            summary = "Update a tag",
            description = "This endpoint allows you to update a specific tag.",
            tags = {"Tags"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Tag updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PutMapping("/{tagId}")
    public ApiResponse<TagResponse> updateTag(@PathVariable("tagId") UUID tagId, @RequestBody TagUpdateRequest request) {
        return ApiResponse.<TagResponse>builder()
                .result(tagService.updateTag(tagId, request))
                .build();
    }

    @Operation(
            summary = "Get blogs by tag",
            description = "This endpoint retrieves blogs associated with a specific tag.",
            tags = {"Tags"},
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Blogs fetched successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))
                    )
            }
    )
    @GetMapping("/blogs/{tagId}")
    public ApiResponse<TagResponse> getBlogsByTagId(@PathVariable("tagId") UUID tagId) {
        return ApiResponse.<TagResponse>builder()
                .result(tagService.getBlogsByTagId(tagId))
                .build();
    }
}
