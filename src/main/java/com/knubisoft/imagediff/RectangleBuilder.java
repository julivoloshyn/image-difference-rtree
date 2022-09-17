package com.knubisoft.imagediff;

import com.knubisoft.node.Node;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RectangleBuilder {

    protected List<Node> createRectangle(double x1, double x2, double y1, double y2) {
        List<Node> horizontalEdges = Stream.of(y1, y2)
                .map(y -> createHorizontalEdge(x1, x2, y))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Node> verticalEdges = Stream.of(x1, x2)
                .map(x -> createVerticalEdges(y1, y2, x))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return Stream.of(horizontalEdges, verticalEdges)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Node> createHorizontalEdge(double x1, double x2, double y) {
        return IntStream.range((int) x1, (int) x2 + 1)
                .boxed()
                .map(x -> new Node(255, x, (int) y))
                .collect(Collectors.toList());
    }

    private List<Node> createVerticalEdges(double y1, double y2, double x) {
        return IntStream.range((int) y1, (int) y2 + 1)
                .boxed()
                .map(y -> new Node(255, (int) x, y))
                .collect(Collectors.toList());
    }
}
