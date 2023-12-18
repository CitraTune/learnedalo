import org.joml.*;
import java.lang.Math;


public class Entity {
    //Id for the entity itself
    private final String id;

    //Used to reference the modelMatrix inside the modelMap
    private final String modelId;

    //Object for the model, holds all the values applied to 1 4x4 matrix.
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private Vector2f rotationVec;
    private Vector3f euler = new Vector3f();
    private float scale;
    private float speed;
    private boolean reversing = false;
    private final float rotationSpeed = 0.01f;
    private final float threshold = 0.0001f;
    private float turnRadius;

    public Entity(String id, String modelId){
        //Associate the model and the entity with id's
        this.id = id;
        this.modelId = modelId;
        //Create the model
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        //Scale is consistent across all 3 dimensions.
        //scale = 0.25f;
    }

    public void accelerate(Float move){
        speed = speed + (move * 0.01f);
        //Maximum speed
        if (speed > 0.05){
            speed = 0.05f;
        }
    }
    public void reverse(Float move){
        reversing = true;
        speed = speed - (move * 0.005f);

    }
    public void moveForward(Float move){
        //Friction
        if (Math.abs(speed) < threshold) {
            speed = 0.0f; // Set speed to zero if it's within the dead zone
        } else {
            if (speed < 0) {
                speed = speed + (0.004f * move);
            } else {
                speed = speed - (0.004f * move);
            }
        }
        //System.out.println(speed);

        Quaternionf rotationQuaternion = rotation;
        // Create a vector representing the direction in which you want to move the object
        Vector3f direction = new Vector3f(0.0f, 0.0f, 1.0f);
        // Use the quaternion to transform the direction vector
        rotationQuaternion.transform(direction);
        // Scale the direction vector by the desired distance to get the translation vector
        direction.normalize().mul(speed * move * 100);
        //System.out.println(direction);
        position.add(direction);
        modelMatrix.identity().translate(position.x, position.y, position.z);

        //recalculate();
    }

    public void steerRight(float move){
        //rotates quaternion
        float hundredSpeed = speed * 300;
        //System.out.println(turnRadius);
        move = -move;
        //angle, then which axis you are rotating on. axis does affect magnitude
        rotation.rotateAxis((125*move/(hundredSpeed + 300) ), 0, 1, 0);
        //applies rotation to the vertex matrix
        modelMatrix.rotate(rotation);
    }
    public void steerLeft(float move){
        //rotates quaternion
        //As speed increases, turning radius should increase due to slipping
        //Hundredspeed is about 200 at max, which is very fast.
        float hundredSpeed = speed * 300;
        //System.out.println(turnRadius);
        //angle, then which axis you are rotating on. axis does affect magnitude
        rotation.rotateAxis((125*move/(hundredSpeed + 300) ), 0, 1, 0);
        //applies rotation to the vertex matrix
        modelMatrix.rotate(rotation);
    }
    public void rotatex(float move){
        rotation.rotateAxis(move, 1, 0, 0);
        //applies rotation to the vertex matrix
        modelMatrix.rotate(rotation);
    }
    public void rotatey(float move){
        rotation.rotateAxis(move, 0, 1, 0);
        //applies rotation to the vertex matrix
        modelMatrix.rotate(rotation);
    }
    public void rotatez(float move){
        rotation.rotateAxis(move, 0, 0, 1);
        //applies rotation to the vertex matrix
        modelMatrix.rotate(rotation);
    }
    public void recalculate(){
        rotation.getEulerAnglesXYZ(euler);
        float maxAngle = 2 * (float) Math.PI;
        euler.y = (euler.y % maxAngle + maxAngle) % maxAngle;
        modelMatrix.identity()
                .rotateX(euler.x)
                .rotateY(euler.y)
                .translate(-position.x, -position.y, -position.z);
    }
    public String getId(){
        return id;
    }
    public String getModelId(){
        return modelId;
    }

    public float getSpeed() {
        return speed;
    }

    public Matrix4f getModelMatrix(){
        return modelMatrix;
    }
    public Vector3f getPosition(){
        return position;
    }
    public Quaternionf getRotation(){
        return rotation;
    }
    public boolean isReversing() {
        return reversing;
    }
    public void setReversing(boolean reversing) {
        this.reversing = reversing;
    }
    public float getScale(){
        return scale;
    }
    //Change the 3 dimension position function. Changes the point all the vertices reference off of.
    public final void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }
    //Change the quaternion with 4 dimensions. I do NOT know how a quaternion works but I know it represents rotation.
    public final void setRotation(float x, float y, float z, float angle){
        this.rotation.fromAxisAngleRad(x,y,z,angle);
    }
    public void setScale(float scale){
        this.scale = scale;
    }
    //Give all the calculated values to the model.
    public void updateModelMatrix(){
        //Converts all translations, rotations, and scalings to the modelMatrix.
        modelMatrix.translationRotateScale(position, rotation, scale);
    }
}
