package com.el.tree;

/**
 * @author Danfeng
 * @since 2019/2/28
 */
public class RHTree {

    private static final boolean RED = false;

    private static class RbNode<T extends Comparable<T>> {

        /**
         * 颜色
         */
        private boolean color;

        /**
         * 关键字
         */
        private T key;

        /**
         * 左子节点
         */
        private RbNode<T> left;

        /**
         * 右子节点
         */
        private RbNode<T> right;

        //父节点
        private RbNode<T> parent;

        public RbNode(boolean color, T key, RbNode<T> left, RbNode<T> right, RbNode<T> parent) {
            this.color = color;
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public T getKey() {
            return key;
        }

        public String toString() {
            return "" + key + (this.color == RED ? "R" : "B");
        }

        /**
         * 左旋示意图：对节点x进行左旋
         * p                       p
         * /                       /
         * x                       y
         * / \                     / \
         * lx  y      ----->       x  ry
         * / \                 / \
         * ly ry               lx ly
         * 左旋做了三件事:
         * 1. 将y的左子节点赋给x的右子节点,并将x赋给y左子节点的父节点(y左子节点非空时)
         * 2. 将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点为y(左或右)
         * 3. 将y的左子节点设为x，将x的父节点设为y
         */
        private void leftRotate(RbNode<T> x) {
            // 1,将Y的左子节点赋给X的右子节点，并将x赋给y左子节点的父节点(y子节点非空时)
            RbNode<T> y = x.right;
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            // 2,将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点;
            y.parent = x.parent;
            if (x.parent == null) {
                x.parent = y;
            } else {
                if (x == x.parent.left) {
                    x.parent.left = y;
                } else {
                    x.parent.right = y;
                }
            }
            // 将y的左子节点设为x，将x的父节点设为y
            x.parent = y;
            y.left = x;
        }

        /*
         * 左旋示意图：对节点y进行右旋
         *        p                   p
         *       /                   /
         *      y                   x
         *     / \                 / \
         *    x  ry   ----->      lx  y
         *   / \                     / \
         * lx  rx                   rx ry
         * 右旋做了三件事：
         * 1. 将x的右子节点赋给y的左子节点,并将y赋给x右子节点的父节点(x右子节点非空时)
         * 2. 将y的父节点p(非空时)赋给x的父节点，同时更新p的子节点为x(左或右)
         * 3. 将x的右子节点设为y，将y的父节点设为x
         */
        private void rightRotate(RbNode<T> y) {
            RbNode<T> x = y.left;
            y.left = x.right;
            if (x.right != null) {
                x.right.parent = y;
            }
            x.parent = y.parent;
            if (y.parent ==null) {
                //this.root = x;
            } else {
                if (y.parent.left == y) {
                    y.parent.left = x;
                } else {
                    y.parent.right = x;
                }
            }
            y.right = x;
            x.parent = y;
        }

    }

}
