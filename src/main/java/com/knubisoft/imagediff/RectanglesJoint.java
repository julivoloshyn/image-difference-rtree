package com.knubisoft.imagediff;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.knubisoft.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RectanglesJoint {

    RectangleBuilder builder = new RectangleBuilder();

    private final List<Node> rectangleBorders = new ArrayList<>();
    private final List<Rectangle> rectangles = new ArrayList<>();
    private RTree<Node, Geometry> groupTree;

    public List<Node> getRectangleBorders() {
        return rectangleBorders;
    }

    protected void getNearest(Entry<Node, Geometry> entry, RTree<Node, Geometry> tree) {
        Node currentNode = entry.value();

        if (isInOtherRectangles(currentNode)) {
            return;
        }

        groupTree = RTree.create();
        tree.nearest(Geometries.point(
                        currentNode.getX(), currentNode.getY()), 150, tree.size())
                .forEach(this::addEntryInTree);

        Optional<Rectangle> mbrOptional;
        if((mbrOptional = groupTree.mbr()).isEmpty()){
            return;
        }

        Rectangle rectangle = mbrOptional.get();
        rectangle.add(rectangle);

        rectangleBorders.addAll(builder.createRectangle(rectangle.x1(), rectangle.x2(),
                rectangle.y1(), rectangle.y2()));
    }


    private boolean isInOtherRectangles(Node currentNode) {
        Optional<Rectangle> optional = rectangles.stream()
                .filter(rectangle -> isInRectangles(rectangle, currentNode))
                .findFirst();

        return optional.isPresent();
    }

    private boolean isInRectangles(Rectangle rectangle, Node currentNode) {
        int x = currentNode.getX();
        int y = currentNode.getY();

        return rectangle.x1() <= x && x <= rectangle.x2() &&
                rectangle.y1() <= y && y <= rectangle.y2();
    }

    private void addEntryInTree(Entry<Node, Geometry> entry) {
        groupTree = groupTree.add(entry.value(), entry.geometry());
    }

}
