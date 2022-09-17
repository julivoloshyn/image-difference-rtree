package com.knubisoft;

import com.knubisoft.imagediff.ImageDifferenceProcessor;

public class Main {
    private static final String file1 = "src/main/resources/image-1.jpg";
    private static final String file2 = "src/main/resources/image-2.jpg";

    public static void main(String[] args) {
        ImageDifferenceProcessor imageDiffProcessor = new ImageDifferenceProcessor();
        imageDiffProcessor.findImageDifference(file1, file2);
    }
}
