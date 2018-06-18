package org.templegalaxia.transform;

import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXVector;

public class CartesianVector {
    public float x;
    public float y;
    public float z;

    public CartesianVector() {}

    public CartesianVector(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public CartesianVector(LXPoint point) {
        this.x += point.x;
        this.y += point.y;
        this.z += point.z;
    }

    public CartesianVector(CartesianVector vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    public CartesianVector(LXVector vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    public CartesianVector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public CartesianVector add(CartesianVector vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
        return this;
    }

    public LXPoint getLXPoint() { return new LXPoint(this.x, this.y, this.z); }

    public LXVector getLXVector() { return new LXVector(this.x, this.y, this.z); }

    public float magnitude(){
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.x * this.x);
    }

    public CartesianVector normalize() {
        return this.scale(1 / this.magnitude());
    }

    public CartesianVector scale(float value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    public CartesianVector setMagnitude(float value) {
        this.normalize();
        this.scale(value);
        return this;
    }
}
