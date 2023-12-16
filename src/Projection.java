import org.joml.Matrix4f;

public class Projection {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_FAR = 1000.f;
    private static final float Z_NEAR = 0.01f;

    private Matrix4f projMatrix;

    public Projection(int width, int height) {
        projMatrix = new Matrix4f();
        updateProjMatrix(width, height);
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public void updateProjMatrix(int width, int height) {
        //setPerspective method, very important
        //Field of view: Sets the amount of degrees you can see in radians. Calculated in the FOV constant
        //Aspect Ratio: The relationship or ratio between the width and the height of the render window.
        //Z_NEAR. The close distance that objects stop being rendered at if they pass
        //Z_FAR. The far distance that objects stop being rendered at if they pass.
        //All of this together creates a perspective instead of orthographic view of the 3d objects. Incredibly useful.
        projMatrix.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
    }
}