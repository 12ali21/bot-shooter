package com.mygdx.botshooter.world.map.quad;

import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Quad<T> {
    public static final int CHUNK_SIZE = 32;

    public static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void update(int x, int y){
            this.x = x;
            this.y = y;
        }
        public Point getChunk(){
            return new Point(x/CHUNK_SIZE, y/CHUNK_SIZE);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    Point botLeft;
    Point topRight;

    Array<T> objects;

    Quad<T> topLQuad;
    Quad<T> topRQuad;
    Quad<T> botLQuad;
    Quad<T> botRQuad;

    public Quad(Point botLeft, Point topRight) {
        this.botLeft = botLeft;
        this.topRight = topRight;
    }

    public boolean insert(T object, Point pos) {
        if (!inBoundary(pos))
            return false;

        // we cannot subdivide this quad further
        if ((Math.abs(botLeft.x - topRight.x) <= CHUNK_SIZE)
                && Math.abs(botLeft.y - topRight.y) <= CHUNK_SIZE) {
            if (objects == null)
                objects = new Array<>();
            objects.add(object);
            return true;
        }
        // left quads
        if ((botLeft.x + topRight.x) / 2 > pos.x) {
            // top left
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topLQuad == null)
                    topLQuad = new Quad<T>(new Point(botLeft.x, (botLeft.y+ topRight.y)/2),
                            new Point((botLeft.x + topRight.x) / 2, topRight.y));
                return topLQuad.insert(object, pos);
            } else { // bottom left
                if (botLQuad == null)
                    botLQuad = new Quad<T>(new Point(botLeft.x, botLeft.y),
                            new Point((botLeft.x + topRight.x) / 2, (botLeft.y+ topRight.y)/2));
                return botLQuad.insert(object, pos);
            }
        } else { // right quads
            // top right
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topRQuad == null)
                    topRQuad = new Quad<T>(new Point((botLeft.x + topRight.x) / 2, (botLeft.y+ topRight.y)/2),
                            new Point(topRight.x, topRight.y));
                return topRQuad.insert(object, pos);
            } else { // bottom right
                if (botRQuad == null)
                    botRQuad = new Quad<T>(new Point((botLeft.x + topRight.x) / 2, botLeft.y),
                            new Point(topRight.x, (botLeft.y+ topRight.y)/2));
                return botRQuad.insert(object, pos);
            }
        }
    }

    public boolean remove(T object, Point pos){
        if (!inBoundary(pos))
            return false;

        // we are on the last quad
        if (objects != null) {
            boolean res = objects.removeValue(object, true);
            return res;
        }

        if ((botLeft.x + topRight.x) / 2 > pos.x) {
            // top left
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topLQuad == null)
                    return false;
                return topLQuad.remove(object, pos);
            }

            // bottom left
            else {
                if (botLQuad == null)
                    return false;
                return botLQuad.remove(object, pos);
            }
        } else {
            // top right
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topRQuad == null)
                    return false;
                return topRQuad.remove(object, pos);
            }

            // bottom right
            else {
                if (botRQuad == null)
                    return false;
                return botRQuad.remove(object, pos);
            }
        }
    }

    /**
     * @param pos position is used to find the chunk
     * @return objects that are in that chunk
     */
    public Array<T> search(Point pos) {
        if (!inBoundary(pos))
            return null;

        // we are on the last quad
        if (objects != null)
            return objects;

        if ((botLeft.x + topRight.x) / 2 > pos.x) {
            // top left
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topLQuad == null)
                    return null;
                return topLQuad.search(pos);
            }

            // bottom left
            else {
                if (botLQuad == null)
                    return null;
                return botLQuad.search(pos);
            }
        } else {
            // top right
            if ((botLeft.y + topRight.y) / 2 <= pos.y) {
                if (topRQuad == null)
                    return null;
                return topRQuad.search(pos);
            }

            // bottom right
            else {
                if (botRQuad == null)
                    return null;
                return botRQuad.search(pos);
            }
        }
    }
    enum Status{UPDATED, REMOVED, NOT_FOUND, NOT_IN_SECTOR}

//    public Status updatePos(T object, Point lastPos, Point newPos){
//        if (!inBoundary(lastPos))
//            return Status.NOT_IN_SECTOR;
//
//        // we are on the last quad
//        if (objects != null) {
//            objects.removeValue(object, true);
//            return Status.REMOVED;
//        }
//
//        Status result;
//        if ((botLeft.x + topRight.x) / 2 > lastPos.x) {
//            // top left
//            if ((botLeft.y + topRight.y) / 2 <= lastPos.y) {
//                if (topLQuad == null)
//                    return Status.NOT_FOUND;
//                result = topLQuad.updatePos(object, lastPos, newPos);
//            }
//
//            // bottom left
//            else {
//                if (botLQuad == null)
//                    return Status.NOT_FOUND;
//                result = botLQuad.updatePos(object, lastPos, newPos);
//            }
//        } else {
//            // top right
//            if ((botLeft.y + topRight.y) / 2 <= lastPos.y) {
//                if (topRQuad == null)
//                    return Status.NOT_FOUND;
//                result = topRQuad.updatePos(object, lastPos, newPos);
//            }
//
//            // bottom right
//            else {
//                if (botRQuad == null)
//                    return Status.NOT_FOUND;
//                result = botRQuad.updatePos(object, lastPos, newPos);
//            }
//        }
//
//        if(result == Status.REMOVED && inBoundary(newPos)){
//            insert(object, newPos);
//            result = Status.UPDATED;
//        }
//        return result;
//    }

    private boolean inBoundary(Point pos) {
        return (pos.x >= botLeft.x && pos.x < topRight.x)
                && (pos.y >= botLeft.y && pos.y < topRight.y);
    }

}
