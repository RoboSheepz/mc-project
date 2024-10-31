package com.elisbite;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

    private long window;
    private World world = new World(); // The world contains all voxel information
    private Player player = new Player(); // The player
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

        // Enable face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Specify to cull back faces
        GL11.glCullFace(GL11.GL_BACK);

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

            // Listen to Player inputs
            player.handleKeyboardInputs(window);
            player.handleMouseInputs(window);
            
            player.tick();

            // Update all blocks
            world.renderWorld( player.getPlayerPos().x, player.getPlayerPos().z, seed );
            
            // Swap the buffers to display the frame
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
