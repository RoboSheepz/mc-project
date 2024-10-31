package com.elisbite;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player {
    private Vector3f playerPosition = new Vector3f(0, 2, 0);
    private Vector3f playerForward = new Vector3f(0, 0, 0);
    private Vector3f playerRight = new Vector3f(0, 0, 0);;
    private float cameraYaw = 0.0f;  // Rotation around Y-axis
    private float cameraPitch = 0.0f; // Rotation around X-axis
    private float cameraSpeed = 0.2f; // Movement speed multiplier

    private UI uiRef;
    Boolean keyToggleF3 = false;

    public void init(UI ui) {
        uiRef = ui;
        //playerPosition = findPlayerSpawnPos();
    }

    public void tick() {
        calculateDirectionInputs();

        // Update camera from mouse position
        glRotatef(cameraPitch, 1.0f, 0.0f, 0.0f); // Pitch (X-axis)
        glRotatef(cameraYaw, 0.0f, 1.0f, 0.0f);   // Yaw (Y-axis)

        glTranslatef(-playerPosition.x, -playerPosition.y, -playerPosition.z);  // Move the world based on camera position
    }

    /**
    * Handles all keyboard input and translates it into vector-based movement
    */
    public void handleKeyboardInputs(long playerWindow) {
        // Direcrion vectors for movement
        float forwardX = (float) Math.sin(Math.toRadians(cameraYaw));
        float forwardZ = (float) -Math.cos(Math.toRadians(cameraYaw));
        float rightX = (float) Math.sin(Math.toRadians(cameraYaw + 90));
        float rightZ = (float) -Math.cos(Math.toRadians(cameraYaw + 90));

        // Move left when the LEFT arrow is pressed
        if (glfwGetKey(playerWindow, GLFW_KEY_A) == GLFW_PRESS) {
            playerPosition.x -= rightX * cameraSpeed;
            playerPosition.z -= rightZ * cameraSpeed;
        }
        if (glfwGetKey(playerWindow, GLFW_KEY_D) == GLFW_PRESS) {
            playerPosition.x += rightX * cameraSpeed;
            playerPosition.z += rightZ * cameraSpeed;
        }

        // Move forward and backward with W/S
        if (glfwGetKey(playerWindow, GLFW_KEY_S) == GLFW_PRESS) {
            playerPosition.x -= forwardX * cameraSpeed;
            playerPosition.z -= forwardZ * cameraSpeed;
        }

        if (glfwGetKey(playerWindow,  GLFW_KEY_W) == GLFW_PRESS) {
            playerPosition.x += forwardX * cameraSpeed;
            playerPosition.z += forwardZ * cameraSpeed;
        }
        // Move up when the SPACE key is pressed
        if (glfwGetKey(playerWindow, GLFW_KEY_SPACE) == GLFW_PRESS) {
            playerPosition.y += cameraSpeed;
        }
        // Move down when the SHIFT key is pressed
        if (glfwGetKey(playerWindow, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            playerPosition.y -= cameraSpeed;
        }
        
        if (glfwGetKey(playerWindow, GLFW_KEY_F3) == GLFW_PRESS) {
            if (!keyToggleF3) {
                uiRef.toggleShowDebugScreen();
                keyToggleF3 = true;
            }
        } else if (glfwGetKey(playerWindow, GLFW_KEY_F3) == GLFW_RELEASE) {
            keyToggleF3 = false;
        }
    }

    /**
    * Handles mouse movement for rotating the first-person camera
    */
    public void handleMouseInputs(long playerWindow) {

        // Get current mouse position
        double[] mousePosX = new double[1];
        double[] mousePosY = new double[1];
        glfwGetCursorPos(playerWindow, mousePosX, mousePosY);

        // calculate change from center (400, 300)
        double deltaX = mousePosX[0] - 400;
        double deltaY = mousePosY[0] - 300;

        // Update yaw and pitch based on mouse movement
        cameraYaw += deltaX * 0.1f;  // Sensitivity
        // Make sure we always keep yaw between 0 and 360
        if (cameraYaw > 360) {
            cameraYaw -= 360;
        } else if (cameraYaw < 0) {
            cameraYaw += 360;
        }

        cameraPitch += deltaY * 0.1f; // Sensitivity

        // Clamp the pitch to prevent gimbal lock
        if (cameraPitch > 89.0f) cameraPitch = 89.0f;
        if (cameraPitch < -89.0f) cameraPitch = -89.0f;

        // Recenter cursor
        glfwSetCursorPos(playerWindow, 400, 300);

    } // end method handleMouse

    private void calculateDirectionInputs(){
        // Convert yaw angle from degrees to radians
        float yawRadian = cameraYaw * ((float) Math.PI / 180.0f);

        playerForward.x = (float) Math.cos(yawRadian);
        playerForward.y = 0;
        playerForward.z = (float) Math.sin(yawRadian);

        playerRight.x = (float) Math.sin(yawRadian);
        playerRight.y = 0;
        playerRight.z = (float) -Math.cos(yawRadian);
    }
    
    public Vector3f getPlayerPos() {
        return playerPosition;
    }

    public float getPlayerYaw(Boolean negativeAngleDisplay) {
        if (negativeAngleDisplay) { 
            if (cameraYaw > 180) {
                return cameraYaw - 360;
            } else {
                return cameraYaw;
            }
        } else {
            return cameraYaw;
        }
    }

    /* public Vector3f findPlayerSpawnPos() {
        return ;
    } */
}
