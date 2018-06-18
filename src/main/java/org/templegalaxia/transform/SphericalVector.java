package org.templegalaxia.transform;

import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXVector;


public class SphericalVector extends CartesianVector {

    public SphericalVector(float radius, float polarAngle, float azimuthalAngle) {
        this.x = sphericalToX(radius, polarAngle, azimuthalAngle);
        this.y = sphericalToY(radius, polarAngle, azimuthalAngle);
        this.z = sphericalToZ(radius, polarAngle, azimuthalAngle);
    }

    private SphericalVector(LXPoint point) {
        super();
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    public SphericalVector(LXVector vector) {
        super();
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public SphericalVector(SphericalVector vector) {
        super();
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public SphericalVector fromCartesian(float x, float y, float z) {
        return new SphericalVector(new LXPoint(x, y, z));
    }

    public float getRadius() {
        return this.magnitude();
    }

    public float getPolarAngle() {
        return (float)Math.acos(this.z / this.magnitude());
    }

    public float getAzimuthalAngle() {
        return (float)Math.atan(this.y / this.x);
    }

    public float sphericalToX(float radius, float polarAngle, float azimuthalAngle) {
        return radius * (float)Math.sin(polarAngle) * (float)Math.cos(azimuthalAngle);
    }

    public float sphericalToY(float radius, float polarAngle, float azimuthalAngle) {
        return radius * (float)Math.sin(polarAngle) * (float)Math.sin(azimuthalAngle);
    }

    public float sphericalToZ(float radius, float polarAngle, float azimuthalAngle) {
        return radius * (float)Math.cos(polarAngle);
    }

    public SphericalVector rotatePolarAngle(float polarAngle) {
        this.x = sphericalToX(getRadius(), getPolarAngle() + polarAngle, getAzimuthalAngle());
        this.y = sphericalToY(getRadius(), getPolarAngle() + polarAngle, getAzimuthalAngle());
        this.z = sphericalToZ(getRadius(), getPolarAngle() + polarAngle, getAzimuthalAngle());

        return this;
    }

    public SphericalVector rotateAzimuthalAngle(float azimuthalAngle) {
        this.x = sphericalToX(getRadius(), getPolarAngle(), getAzimuthalAngle() + azimuthalAngle);
        this.y = sphericalToY(getRadius(), getPolarAngle(), getAzimuthalAngle() + azimuthalAngle);

        return this;
    }
}