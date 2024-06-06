package com.cafehub.cafehub.review.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {

    private Long reviewId;

    private Integer reviewRating;

    private List<MultipartFile> photos;

    private String reviewContent;
}
