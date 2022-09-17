package com.knubisoft.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Node {
    private int pixel;
    private int x;
    private int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        if(this.getPixel() != node.getPixel()){
            return false;
        }
        if(this.getX() != node.getX()){
            return false;
        }

        return this.getY() != 0 ? this.getY() == (node.getY()) : node.getY() == 0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
