import org.joml.*;

public class Entity {
    //Id for the entity itself
    private final String id;

    //Used to reference the modelMatrix inside the modelMap
    private final String modelId;

    //Object for the model, holds all the values applied to 1 4x4 matrix.
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;

    public Entity(String id, String modelId){
        //Associate the model and the entity with id's
        this.id = id;
        this.modelId = modelId;
        //Create the model
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        //Scale is consistent across all 3 dimensions.
        scale = 1;
    }
    public String getId(){
        return id;
    }
    public String getModelId(){
        return modelId;
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
