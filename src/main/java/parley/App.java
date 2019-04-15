/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package parley;

import javax.swing.*;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }
}
