package com.knubisoft.imagediff;

import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.knubisoft.node.Node;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class ImageDifferenceProcessor {

    RectanglesJoint joint = new RectanglesJoint();

    private static final String PATH1 = "src/main/resources/image-1.jpg";
    private static final String PATH2 = "src/main/resources/image-2.jpg";

    public void findImageDifference(String input1, String input2) {
        BufferedImage[] bufferedImages = getImageIo(input1, input2);
        RTree<Node, Geometry> tree = createRTree(bufferedImages[0], bufferedImages[1]);
        createRectangleFromTree(tree);

        createResultImage(bufferedImages[0], bufferedImages[1], joint.getRectangleBorders());
    }

    @SneakyThrows
    private BufferedImage[] getImageIo(String input1, String input2) {
        File file1 = Paths.get(input1).toFile();
        File file2 = Paths.get(input2).toFile();

        BufferedImage image1 = ImageIO.read(file1);
        BufferedImage image2 = ImageIO.read(file2);

        return new BufferedImage[]{image1, image2};
    }

    private RTree<Node, Geometry> createRTree(BufferedImage image1, BufferedImage image2) {
        RTree<Node, Geometry> tree = RTree.maxChildren(6).create();

        if (image1.getHeight() != image2.getHeight() || image1.getWidth() != image2.getWidth()) {
            throw new RuntimeException("Images size are different.");
        }

        for (int x = 0; x < image1.getWidth(); x++) {
            for (int y = 0; y < image1.getHeight(); y++) {

                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    Node node = new Node(image1.getRGB(x, y), x, y);
                    tree = tree.add(node, Geometries.point(x, y));
                }
            }
        }
        return tree;
    }

    private void createRectangleFromTree(RTree<Node, Geometry> tree) {
        tree.entries().forEach(geometryEntry -> joint.getNearest(geometryEntry, tree));
    }

    @SneakyThrows
    private void createResultImage(BufferedImage image1,
                                   BufferedImage image2,
                                   List<Node> rectangleBorders) {

        for(Node node : rectangleBorders) {
            image1.setRGB(node.getX(), node.getY(), node.getPixel());
            image2.setRGB(node.getX(), node.getY(), node.getPixel());
        }

        File file1 = new File(PATH1);
        File file2 = new File(PATH2);

        ImageIO.write(image1, "jpg", file1);
        ImageIO.write(image2, "jpg", file2);
    }

}
