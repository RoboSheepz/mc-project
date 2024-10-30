package com.elisbite;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

    private long window;
    private float cameraX = 0; // camera position
    private float cameraY = 2;
    private float cameraZ = 0;
    private float cameraYaw = 0.0f;  // Rotation around Y-axis
    private float cameraPitch = 0.0f; // Rotation around X-axis
    private float cameraSpeed = 0.2f; // Movement speed multiplier
    private World world = new World(); // The world contains all voxel information
    private int seed = 100;

    /**
     * Handles initialization, game loop, and closing game
     */
    public void run() {
        System.out.println("Starting the game...");
        init();
        loop();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    /**
     * Sets up GL including window, camera, rendering, cursor
     */
    private void init() {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure the window properties
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(800, 600, "Anyacraft", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - 800) / 2, (vidMode.height() - 600) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable V-Sync
        glfwShowWindow(window);

        // Create OpenGL capabilities
        GL.createCapabilities();

        // Enable depth testing for 3D rendering
        glEnable(GL_DEPTH_TEST);

        // Set up the projection matrix for perspective
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspect = 800f / 600f;
        glFrustum(-aspect, aspect, -1.0, 1.0, 1.0, 100.0);

        // Hide cursor and capture it
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetCursorPos(window, 400, 300); // Center the cursor

        // Switch back to model view matrix
        glMatrixMode(GL_MODELVIEW);
    }

    /**
     * The main game loop: swapping buffers, moving camera
     */
    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            // Clear the color and depth buffers
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Reset the model-view matrix
            glLoadIdentity();

            // Move the camera back so the cube is visible
            handleInput();
            handleMouse();

            // Update camera from mouse position
            glRotatef(cameraPitch, 1.0f, 0.0f, 0.0f); // Pitch (X-axis)
            glRotatef(cameraYaw, 0.0f, 1.0f, 0.0f);   // Yaw (Y-axis)

            glTranslatef(cameraX, cameraY, cameraZ);  // Move the world based on camera position

            // Update all blocks
            world.renderWorld( cameraX, cameraZ, seed );
            
            // Swap the buffers to display the frame
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    /**
     * Handles all keyboard input and translates it into vector-based movement
     */
    private void handleInput() {

        // Direcrion vectors for movement
        float forwardX = (float) Math.sin(Math.toRadians(cameraYaw));
        float forwardZ = (float) -Math.cos(Math.toRadians(cameraYaw));
        float rightX = (float) Math.sin(Math.toRadians(cameraYaw + 90));
        float rightZ = (float) -Math.cos(Math.toRadians(cameraYaw + 90));

        // Move left when the LEFT arrow is pressed
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            cameraX += rightX * cameraSpeed;
            cameraZ += rightZ * cameraSpeed;
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            cameraX -= rightX * cameraSpeed;
            cameraZ -= rightZ * cameraSpeed;
        }

        // Move forward and backward with W/S
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            cameraX += forwardX * cameraSpeed;
            cameraZ += forwardZ * cameraSpeed;
        }

        if (glfwGetKey(window,  GLFW_KEY_W) == GLFW_PRESS) {
            cameraX -= forwardX * cameraSpeed;
            cameraZ -= forwardZ * cameraSpeed;
        }
        // Move up when the SPACE key is pressed
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            cameraY -= cameraSpeed;
        }
        // Move down when the SHIFT key is pressed
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            cameraY += cameraSpeed;
        }
    }

    /**
     * Handles mouse movement for rotating the first-person camera
     */
    private void handleMouse() {

        // Get current mouse position
        double[] mousePosX = new double[1];
        double[] mousePosY = new double[1];
        glfwGetCursorPos(window, mousePosX, mousePosY);

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
        glfwSetCursorPos(window, 400, 300);

    } // end method handleMouse

    public static void main(String[] args) {
        new Game().run();

    }
}
