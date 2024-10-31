package com.elisbite;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player {
    private Vector3f playerPosition = new Vector3f(0, 2, 0);
    private float cameraYaw = 0.0f;  // Rotation around Y-axis
    private float cameraPitch = 0.0f; // Rotation around X-axis
    private float cameraSpeed = 0.2f; // Movement speed multiplier

    public void init() {
        //playerPosition = findPlayerSpawnPos();
    }

    public void tick() {
        // Update camera from mouse position
        glRotatef(cameraPitch, 1.0f, 0.0f, 0.0f); // Pitch (X-axis)
        glRotatef(cameraYaw, 0.0f, 1.0f, 0.0f);   // Yaw (Y-axis)

        glTranslatef(playerPosition.x, playerPosition.y, playerPosition.z);  // Move the world based on camera position
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
            playerPosition.x += rightX * cameraSpeed;
            playerPosition.z += rightZ * cameraSpeed;
        }
        if (glfwGetKey(playerWindow, GLFW_KEY_D) == GLFW_PRESS) {
            playerPosition.x -= rightX * cameraSpeed;
            playerPosition.z -= rightZ * cameraSpeed;
        }

        // Move forward and backward with W/S
        if (glfwGetKey(playerWindow, GLFW_KEY_S) == GLFW_PRESS) {
            playerPosition.x += forwardX * cameraSpeed;
            playerPosition.z += forwardZ * cameraSpeed;
        }

        if (glfwGetKey(playerWindow,  GLFW_KEY_W) == GLFW_PRESS) {
            playerPosition.x -= forwardX * cameraSpeed;
            playerPosition.z -= forwardZ * cameraSpeed;
        }
        // Move up when the SPACE key is pressed
        if (glfwGetKey(playerWindow, GLFW_KEY_SPACE) == GLFW_PRESS) {
            playerPosition.y -= cameraSpeed;
        }
        // Move down when the SHIFT key is pressed
        if (glfwGetKey(playerWindow, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            playerPosition.y += cameraSpeed;
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
        cameraPitch += deltaY * 0.1f; // Sensitivity

        // Clamp the pitch to prevent gimbal lock
        if (cameraPitch > 89.0f) cameraPitch = 89.0f;
        if (cameraPitch < -89.0f) cameraPitch = -89.0f;

        // Recenter cursor
        glfwSetCursorPos(playerWindow, 400, 300);

    } // end method handleMouse

    public Vector3f getPlayerPos() {
        return playerPosition;
    }

    /* public Vector3f findPlayerSpawnPos() {
        return ;
    } */
}
