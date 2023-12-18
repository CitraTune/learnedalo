

import org.joml.*;

import java.lang.Math;

public class Camera {

    private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Quaternionf rotationQ;
    private Vector3f up;
    private Matrix4f viewMatrix;
    private float speed;
    private Vector3f pivoffset = new Vector3f(0.0f, 0.24f, 0.25f);

    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
        rotationQ = new Quaternionf();

    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);
        recalculate();
    }


    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    public void accelerate(Float move){
        speed = speed + (move * 0.01f);
        //Maximum speed
        if (speed > 0.05){
            speed = 0.05f;
        }
    }
    public void reverse(Float move){
        speed = speed - (move * 0.005f);

    }
    public void moveForward(Float move){
        //Friction
        if (Math.abs(speed) < 0.0001f) {
            speed = 0.0f; // Set speed to zero if it's within the dead zone
        } else {
            if (speed < 0) {
                speed = speed + (0.004f * move);
            } else {
                speed = speed - (0.004f * move);
            }
        }
        //System.out.println(speed);

        Quaternionf rotationQuaternion = rotationQ;
        // Create a vector representing the direction in which you want to move the object
        Vector3f direction = new Vector3f(0.0f, 0.0f, 1.0f);

        // Use the quaternion to transform the direction vector
        rotationQuaternion.transform(direction);
        // Scale the direction vector by the desired distance to get the translation vector
        direction.normalize().mul(-speed * move * 100);
        //System.out.println(direction);
        position.add(direction);
        //viewMatrix.identity().translate(position.x, position.y, position.z);

        recalculate();
    }

    public void steerRight(float move){
        //rotates quaternion
        float hundredSpeed = speed * 300;
        //System.out.println(turnRadius);
        move = -move;
        //angle, then which axis you are rotating on. axis does affect magnitude
        rotationQ.rotateAxis((125*move/(hundredSpeed + 300) ), 0, 1, 0);
        //applies rotation to the vertex matrix
        //viewMatrix.rotate(rotationQ);
        position.x = Main.getCarEntity().getPosition().x * Math.cos();
        recalculate();

    }

    public void steerLeft(float move){
        //rotates quaternion
        //As speed increases, turning radius should increase due to slipping
        //Hundredspeed is about 200 at max, which is very fast.
        float hundredSpeed = speed * 300;
        //System.out.println(turnRadius);
        move = -move;
        //angle, then which axis you are rotating on. axis does affect magnitude
        rotationQ.rotateAxis((125*move/(hundredSpeed + 300) ), 0, 1, 0);
        //applies rotation to the vertex matrix
        //viewMatrix.rotate(rotationQ);
        viewMatrix.identity().lookAt(
                Main.getCarEntity().getPosition().x, Main.getCarEntity().getPosition().y + 5.0f, Main.getCarEntity().getPosition().z + 10.0f,  // Camera position
                Main.getCarEntity().getPosition().x, Main.getCarEntity().getPosition().y, Main.getCarEntity().getPosition().z,                 // Look at the car
                0.0f, 1.0f, 0.0f                                              // Up direction
        );
        //addRotation(0,(125*move/(hundredSpeed + 300) ));
    }


    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }

    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }

    public void moveForward2(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }

    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }

    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }

    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        recalculate();
    }

    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }
}